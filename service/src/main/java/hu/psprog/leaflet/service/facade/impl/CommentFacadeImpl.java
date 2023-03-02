package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CommentFacade;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link CommentFacade}.
 *
 * @author Peter Smith
 */
@Service
public class CommentFacadeImpl implements CommentFacade {

    private static final List<GrantedAuthority> NO_LOGIN_AUTHORITY = AuthorityUtils.createAuthorityList(Role.NO_LOGIN.name());
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentFacadeImpl.class);
    private static final String COMMENT_NOTIFICATION_ENABLED = "${mail.event.comment-notification.enabled}";

    private final CommentService commentService;
    private final UserService userService;
    private final EntryService entryService;
    private final boolean commentNotificationEnabled;

    @Autowired
    public CommentFacadeImpl(CommentService commentService, UserService userService, EntryService entryService,
                             @Value(COMMENT_NOTIFICATION_ENABLED) boolean commentNotificationEnabled) {
        this.commentService = commentService;
        this.userService = userService;
        this.entryService = entryService;
        this.commentNotificationEnabled = commentNotificationEnabled;
    }

    @Override
    public void deletePermanently(Long id) throws ServiceException {
        commentService.deleteByID(id);
    }

    @Override
    public void deleteLogically(Long id) throws ServiceException {
        CommentVO commentVO = commentService.getOne(id);
        commentService.deleteLogicallyByEntity(commentVO);
    }

    @Override
    public void restoreEntity(Long id) throws ServiceException {
        CommentVO commentVO = commentService.getOne(id);
        commentService.restoreEntity(commentVO);
    }

    @Override
    public CommentVO changeStatus(Long id) throws ServiceException {

        CommentVO commentVO = commentService.getOne(id);
        if (commentVO.isEnabled()) {
            commentService.disable(id);
        } else {
            commentService.enable(id);
        }

        return commentService.getOne(id);
    }

    @Override
    public CommentVO getOne(Long id) throws ServiceException {
        return commentService.getOne(id);
    }

    @Override
    public List<CommentVO> getAll() {
        return commentService.getAll();
    }

    @Override
    public Long createOne(CommentVO entity) throws ServiceException {

        CommentVO commentToBeCreated = Objects.isNull(entity.getOwner().getId())
                ? recreateCommentWithUserAttached(entity)
                : entity;

        Long commentID = commentService.createOne(commentToBeCreated);

        if (commentNotificationEnabled) {
            commentService.notifyEntryAuthor(commentID);
        }

        return commentID;
    }

    @Override
    public CommentVO updateOne(Long id, CommentVO updatedEntity) throws ServiceException {
        commentService.updateOne(id, updatedEntity);
        return commentService.getOne(id);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfCommentsForEntry(Long entryID, int page, int limit, String direction, String orderBy) {
        return commentService.getPageOfCommentsForEntry(page, limit, parseDirection(direction), parseOrderBy(orderBy), EntryVO.wrapMinimumVO(entryID));
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfPublicCommentsForEntry(String entryLink, int page, int limit, String direction, String orderBy) {

        EntryVO entryVO = null;
        try {
            entryVO = entryService.findByLink(entryLink);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Comments requested for non-existing entry by link [{}].", entryLink, e);
        }

        return commentService.getPageOfPublicCommentsForEntry(page, limit, parseDirection(direction), parseOrderBy(orderBy), entryVO);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfCommentsForUser(Long userID, int page, int limit, String direction, String orderBy) {
        return commentService.getPageOfCommentsForUser(page, limit, parseDirection(direction), parseOrderBy(orderBy), UserVO.wrapMinimumVO(userID));
    }

    private CommentVO.OrderBy parseOrderBy(String by) {

        CommentVO.OrderBy orderBy = CommentVO.OrderBy.CREATED;
        try {
            orderBy = CommentVO.OrderBy.valueOf(by.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unknown order by constant [{}] specified, falling back to OrderBy.CREATED value.", by, e);
        }

        return orderBy;
    }

    private OrderDirection parseDirection(String direction) {

        OrderDirection orderDirection = OrderDirection.ASC;
        try {
            orderDirection = OrderDirection.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unknown order direction constant [{}] specified, falling back to OrderDirection.ASC value.", direction, e);
        }

        return orderDirection;
    }

    private CommentVO recreateCommentWithUserAttached(CommentVO entity) throws ServiceException {

        CommentVO commentToBeCreated;
        UserVO user = userService.silentGetUserByEmail(entity.getOwner().getEmail());

        if (Objects.isNull(user)) {
            LOGGER.info("Registering anonymous commenter [{}]", entity.getOwner().getEmail());
            Long id = userService.registerNoLogin(createNoLoginUser(entity.getOwner()));
            commentToBeCreated = updateCommentOwner(entity, id);

        } else if (isReturningNoLoginUser(user)) {
            LOGGER.info("Attaching comment to returning anonymous commenter [{}]", entity.getOwner().getEmail());
            commentToBeCreated = updateCommentOwner(entity, user.getId());

        } else {
            LOGGER.error("Login-enabled user already exists with given email={}", entity.getOwner().getEmail());
            throw new EntityCreationException(Comment.class);
        }

        return commentToBeCreated;
    }

    private CommentVO updateCommentOwner(CommentVO comment, Long updatedUserID) {

        return CommentVO.getBuilder()
                .withContent(comment.getContent())
                .withOwner(UserVO.getBuilder()
                        .withId(updatedUserID)
                        .withEmail(comment.getOwner().getEmail())
                        .withUsername(comment.getOwner().getUsername())
                        .build())
                .withEntryVO(comment.getEntryVO())
                .build();
    }

    private UserVO createNoLoginUser(UserVO owner) {

        return UserVO.getBuilder()
                .withEmail(owner.getEmail())
                .withUsername(owner.getUsername())
                .withAuthorities(NO_LOGIN_AUTHORITY)
                .withEnabled(true)
                .withLocale(Locale.EN)
                .build();
    }

    private boolean isReturningNoLoginUser(UserVO user) {
        return user.getAuthorities().containsAll(NO_LOGIN_AUTHORITY);
    }
}
