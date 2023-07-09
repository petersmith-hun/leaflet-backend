package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CommentToCommentVOConverter;
import hu.psprog.leaflet.service.converter.CommentVOToCommentConverter;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.search.SearchHandler;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.service.vo.mail.CommentNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CommentService}.
 *
 * @author Peter Smith
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);
    private static final EntityPageVO<CommentVO> EMPTY_ENTITY_PAGE_VO = EntityPageVO.<CommentVO>getBuilder()
            .withEntitiesOnPage(Collections.emptyList())
            .build();

    private final CommentDAO commentDAO;
    private final CommentToCommentVOConverter commentToCommentVOConverter;
    private final CommentVOToCommentConverter commentVOToCommentConverter;
    private final NotificationService notificationService;
    private final SearchHandler<CommentSearchParametersVO, Comment> searchHandler;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO, CommentToCommentVOConverter commentToCommentVOConverter,
                              CommentVOToCommentConverter commentVOToCommentConverter, NotificationService notificationService,
                              SearchHandler<CommentSearchParametersVO, Comment> searchHandler) {
        this.commentDAO = commentDAO;
        this.commentToCommentVOConverter = commentToCommentVOConverter;
        this.commentVOToCommentConverter = commentVOToCommentConverter;
        this.notificationService = notificationService;
        this.searchHandler = searchHandler;
    }

    @Override
    @PermitScope.Read.OwnCommentsOrElevated
    public CommentVO getOne(Long id) throws ServiceException {

        return commentDAO.findById(id)
                .map(commentToCommentVOConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));
    }

    @Override
    @PermitScope.Read.Comments
    public List<CommentVO> getAll() {

        return commentDAO.findAll().stream()
                .map(commentToCommentVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitScope.Read.Comments
    public EntityPageVO<CommentVO> getPageOfCommentsForEntry(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, EntryVO entryVO) {

        var commentSearchParametersVO = searchBuilder(page, limit, direction, orderBy)
                .entryID(Optional.of(entryVO.getId()))
                .build();

        return getPageWithWhereSpecification(commentSearchParametersVO);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfPublicCommentsForEntry(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, EntryVO entryVO) {

        EntityPageVO<CommentVO> entityPageVO = EMPTY_ENTITY_PAGE_VO;
        if (Objects.nonNull(entryVO)) {

            var commentSearchParametersVO = searchBuilder(page, limit, direction, orderBy)
                    .enabled(Optional.of(true))
                    .entryID(Optional.of(entryVO.getId()))
                    .build();

            entityPageVO = getPageWithWhereSpecification(commentSearchParametersVO);
        }

        return entityPageVO;
    }

    @Override
    @PermitScope.Read.OwnCommentsListOrElevated
    public EntityPageVO<CommentVO> getPageOfCommentsForUser(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, UserVO userVO) {

        var commentSearchParametersVO = searchBuilder(page, limit, direction, orderBy)
                .userID(Optional.of(userVO.getId()))
                .build();

        return getPageWithWhereSpecification(commentSearchParametersVO);
    }

    @Override
    @PermitScope.Read.Comments
    public EntityPageVO<CommentVO> searchComments(CommentSearchParametersVO commentSearchParametersVO) {
        return getPageWithWhereSpecification(commentSearchParametersVO);
    }

    @Override
    public void notifyEntryAuthor(Long commentID) {

        commentDAO.findById(commentID)
                .map(comment -> CommentNotification.getBuilder()
                        .withUsername(comment.getUser().getUsername())
                        .withEmail(comment.getUser().getEmail())
                        .withContent(comment.getContent())
                        .withEntryTitle(comment.getEntry().getTitle())
                        .withAuthorEmail(comment.getEntry().getUser().getEmail())
                        .withAuthorName(comment.getEntry().getUser().getUsername())
                        .build())
                .ifPresent(notificationService::commentNotification);
    }

    @Override
    @PermitScope.Write.CreateComment
    public Long createOne(CommentVO entity) throws ServiceException {

        Comment comment = commentVOToCommentConverter.convert(entity);
        Comment savedComment = commentDAO.save(comment);

        LOGGER.info("New comment has been created by user [{}] with ID [{}]", savedComment.getUser().getId(), savedComment.getId());

        return savedComment.getId();
    }

    @Override
    @PermitScope.Write.OwnCommentOrElevated
    public CommentVO updateOne(Long id, CommentVO updatedEntity) throws ServiceException {

        Comment updatedComment = commentDAO.updateOne(id, commentVOToCommentConverter.convert(updatedEntity))
                .orElseThrow(() -> new EntityNotFoundException(Entry.class, id));

        LOGGER.info("Comment of ID [{}] has been updated by user [{}]", id, updatedComment.getUser().getId());

        return commentToCommentVOConverter.convert(updatedComment);
    }

    @Override
    @PermitScope.Write.Admin
    public void deleteByID(Long id) throws ServiceException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.delete(id);
        LOGGER.info("Deleted comment of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Comments
    public void enable(Long id) throws EntityNotFoundException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.enable(id);
        LOGGER.info("Enabled comment of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Comments
    public void disable(Long id) throws EntityNotFoundException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.disable(id);
        LOGGER.info("Disabled comment of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.OwnCommentByEntityOrElevated
    public void deleteLogicallyByEntity(CommentVO entity) throws ServiceException {

        if (!commentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Comment.class, entity.getId());
        }

        commentDAO.markAsDeleted(entity.getId());
        LOGGER.info("Marked comment of ID [{}] as logically deleted", entity.getId());
    }

    @Override
    @PermitScope.Write.Comments
    public void restoreEntity(CommentVO entity) throws ServiceException {

        if (!commentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Comment.class, entity.getId());
        }

        commentDAO.revertLogicalDeletion(entity.getId());
        LOGGER.info("Restored logically deleted comment of ID [{}]", entity.getId());
    }

    private EntityPageVO<CommentVO> getPageWithWhereSpecification(CommentSearchParametersVO commentSearchParametersVO) {

        Pageable pageable = PageableUtil.createPage(
                commentSearchParametersVO.getPage(),
                commentSearchParametersVO.getLimit(),
                commentSearchParametersVO.getOrderDirection(),
                commentSearchParametersVO.getOrderBy().getField());
        Specification<Comment> specification = searchHandler.createSpecification(commentSearchParametersVO);
        Page<Comment> commentPage = commentDAO.findAll(specification, pageable);

        return PageableUtil.convertPage(commentPage, commentToCommentVOConverter);
    }

    private CommentSearchParametersVO.CommentSearchParametersVOBuilder searchBuilder(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy) {

        return CommentSearchParametersVO.builder()
                .page(page)
                .limit(limit)
                .orderDirection(direction)
                .orderBy(orderBy);
    }
}
