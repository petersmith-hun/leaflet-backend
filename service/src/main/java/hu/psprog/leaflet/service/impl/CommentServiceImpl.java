package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CommentToCommentVOConverter;
import hu.psprog.leaflet.service.converter.CommentVOToCommentConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.security.annotation.PermitAdmin;
import hu.psprog.leaflet.service.security.annotation.PermitEditorOrAdmin;
import hu.psprog.leaflet.service.security.annotation.PermitSelf;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
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
import java.util.stream.Collectors;

/**
 * Implementation of {@link CommentService}.
 *
 * @author Peter Smith
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);
    private static final EntityPageVO<CommentVO> EMPTY_ENTITY_PAGE_VO = EntityPageVO.getBuilder()
            .withEntitiesOnPage(Collections.emptyList())
            .build();

    private CommentDAO commentDAO;
    private CommentToCommentVOConverter commentToCommentVOConverter;
    private CommentVOToCommentConverter commentVOToCommentConverter;
    private EntryVOToEntryConverter entryVOToEntryConverter;
    private UserVOToUserConverter userVOToUserConverter;
    private NotificationService notificationService;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO, CommentToCommentVOConverter commentToCommentVOConverter,
                              CommentVOToCommentConverter commentVOToCommentConverter, EntryVOToEntryConverter entryVOToEntryConverter,
                              UserVOToUserConverter userVOToUserConverter, NotificationService notificationService) {
        this.commentDAO = commentDAO;
        this.commentToCommentVOConverter = commentToCommentVOConverter;
        this.commentVOToCommentConverter = commentVOToCommentConverter;
        this.entryVOToEntryConverter = entryVOToEntryConverter;
        this.userVOToUserConverter = userVOToUserConverter;
        this.notificationService = notificationService;
    }

    @Override
    @PermitSelf.Comment
    public CommentVO getOne(Long id) throws ServiceException {

        Comment comment = commentDAO.findOne(id);

        if (comment == null) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        return commentToCommentVOConverter.convert(comment);
    }

    @Override
    @PermitEditorOrAdmin
    public List<CommentVO> getAll() {

        return commentDAO.findAll().stream()
                .map(commentToCommentVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitEditorOrAdmin
    public EntityPageVO<CommentVO> getPageOfCommentsForEntry(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, EntryVO entryVO) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Entry entry = entryVOToEntryConverter.convert(entryVO);
        Page<Comment> commentPage = commentDAO.findByEntry(pageable, entry);

        return PageableUtil.convertPage(commentPage, commentToCommentVOConverter);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfPublicCommentsForEntry(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, EntryVO entryVO) {

        EntityPageVO<CommentVO> entityPageVO = EMPTY_ENTITY_PAGE_VO;
        if (Objects.nonNull(entryVO)) {
            Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
            Entry entry = entryVOToEntryConverter.convert(entryVO);
            Specification<Comment> specifications = Specification
                    .where(CommentSpecification.IS_ENABLED);
            Page<Comment> commentPage = commentDAO.findByEntry(specifications, pageable, entry);

            entityPageVO = PageableUtil.convertPage(commentPage, commentToCommentVOConverter);
        }

        return entityPageVO;
    }

    @Override
    @PermitSelf.UserOrModerator
    public EntityPageVO<CommentVO> getPageOfCommentsForUser(int page, int limit, OrderDirection direction, CommentVO.OrderBy orderBy, UserVO userVO) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        User user = userVOToUserConverter.convert(userVO);
        Page<Comment> commentPage = commentDAO.findByUser(pageable, user);

        return PageableUtil.convertPage(commentPage, commentToCommentVOConverter);
    }

    @Override
    public void notifyEntryAuthor(Long commentID) {

        Comment comment = commentDAO.findOne(commentID);
        notificationService.commentNotification(CommentNotification.getBuilder()
                .withUsername(comment.getUser().getUsername())
                .withEmail(comment.getUser().getEmail())
                .withContent(comment.getContent())
                .withEntryTitle(comment.getEntry().getTitle())
                .withAuthorEmail(comment.getEntry().getUser().getEmail())
                .withAuthorName(comment.getEntry().getUser().getUsername())
                .build());
    }

    @Override
    @PermitEditorOrAdmin
    public Long count() {

        return commentDAO.count();
    }

    @Override
    public Long createOne(CommentVO entity) throws ServiceException {

        Comment comment = commentVOToCommentConverter.convert(entity);
        Comment savedComment = commentDAO.save(comment);

        if (savedComment == null) {
            throw new EntityCreationException(Comment.class);
        }

        LOGGER.info("New comment has been created by user [{}] with ID [{}]", savedComment.getUser().getId(), savedComment.getId());

        return savedComment.getId();
    }

    @Override
    @PermitSelf.Comment
    public CommentVO updateOne(Long id, CommentVO updatedEntity) throws ServiceException {

        Comment updatedComment = commentDAO.updateOne(id, commentVOToCommentConverter.convert(updatedEntity));

        if (updatedComment == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        LOGGER.info("Comment of ID [{}] has been updated by user [{}]", id, updatedComment.getUser().getId());

        return commentToCommentVOConverter.convert(updatedComment);
    }

    @Override
    @PermitAdmin
    public void deleteByID(Long id) throws ServiceException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.delete(id);
        LOGGER.info("Deleted comment of ID [{}]", id);
    }

    @Override
    @PermitEditorOrAdmin
    public void enable(Long id) throws EntityNotFoundException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.enable(id);
        LOGGER.info("Enabled comment of ID [{}]", id);
    }

    @Override
    @PermitEditorOrAdmin
    public void disable(Long id) throws EntityNotFoundException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.disable(id);
        LOGGER.info("Disabled comment of ID [{}]", id);
    }

    @Override
    @PermitSelf.CommentByEntity
    public void deleteLogicallyByEntity(CommentVO entity) throws ServiceException {

        if (!commentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Comment.class, entity.getId());
        }

        commentDAO.markAsDeleted(entity.getId());
        LOGGER.info("Marked comment of ID [{}] as logically deleted", entity.getId());
    }

    @Override
    @PermitEditorOrAdmin
    public void restoreEntity(CommentVO entity) throws ServiceException {

        if (!commentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Comment.class, entity.getId());
        }

        commentDAO.revertLogicalDeletion(entity.getId());
        LOGGER.info("Restored logically deleted comment of ID [{}]", entity.getId());
    }


}
