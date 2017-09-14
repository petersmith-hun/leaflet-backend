package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.service.CommentService;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@inheritDoc}
 *
 * @author Peter Smith
 */
@Service
public class CommentFacadeImpl implements CommentFacade {

    private static final List<GrantedAuthority> NO_LOGIN_AUTHORITY = AuthorityUtils.createAuthorityList(Role.NO_LOGIN.name());
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentFacadeImpl.class);

    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentFacadeImpl(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {
        commentService.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {
        commentService.disable(id);
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {
        commentService.deleteByID(id);
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {
        commentService.deleteBulkByIDs(ids);
    }

    @Override
    public void deleteLogicallyByEntity(CommentVO entity) throws ServiceException {
        commentService.deleteLogicallyByEntity(entity);
    }

    @Override
    public void restoreEntity(CommentVO entity) throws ServiceException {
        commentService.restoreEntity(entity);
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
    public Long count() {
        return commentService.count();
    }

    @Override
    public Long createOne(CommentVO entity) throws ServiceException {
        CommentVO commentToBeCreated = entity;
        if (Objects.isNull(entity.getOwner().getId())) {
            UserVO user = userService.silentGetUserByEmail(entity.getOwner().getEmail());
            if (Objects.isNull(user)) {
                Long id = userService.createOne(createNoLoginUser(entity.getOwner()));
                commentToBeCreated = updateCommentOwner(entity, id);
            } else {
                if (user.getAuthorities().containsAll(NO_LOGIN_AUTHORITY)) {
                    commentToBeCreated = updateCommentOwner(entity, user.getId());
                } else {
                    LOGGER.error("Login-enabled user already exists with given email={}", entity.getOwner().getEmail());
                    throw new EntityCreationException(Comment.class);
                }
            }
        }

        return commentService.createOne(commentToBeCreated);
    }

    @Override
    public List<Long> createBulk(List<CommentVO> entities) throws ServiceException {
        return commentService.createBulk(entities);
    }

    @Override
    public CommentVO updateOne(Long id, CommentVO updatedEntity) throws ServiceException {
        return commentService.updateOne(id, updatedEntity);
    }

    @Override
    public List<CommentVO> updateBulk(Map<Long, CommentVO> updatedEntities) throws ServiceException {
        return commentService.updateBulk(updatedEntities);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfCommentsForEntry(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, EntryVO entryVO) {
        return commentService.getPageOfCommentsForEntry(page, limit, direction, orderBy, entryVO);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfPublicCommentsForEntry(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, EntryVO entryVO) {
        return commentService.getPageOfPublicCommentsForEntry(page, limit, direction, orderBy, entryVO);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfCommentsForUser(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, UserVO userVO) {
        return commentService.getPageOfCommentsForUser(page, limit, direction, orderBy, userVO);
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
                .build();
    }
}
