package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CommentToCommentVOConverter;
import hu.psprog.leaflet.service.converter.CommentVOToCommentConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
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
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CommentService}.
 *
 * @author Peter Smith
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private CommentDAO commentDAO;
    private CommentToCommentVOConverter commentToCommentVOConverter;
    private CommentVOToCommentConverter commentVOToCommentConverter;
    private EntryVOToEntryConverter entryVOToEntryConverter;
    private UserVOToUserConverter userVOToUserConverter;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO, CommentToCommentVOConverter commentToCommentVOConverter,
                              CommentVOToCommentConverter commentVOToCommentConverter, EntryVOToEntryConverter entryVOToEntryConverter,
                              UserVOToUserConverter userVOToUserConverter) {
        this.commentDAO = commentDAO;
        this.commentToCommentVOConverter = commentToCommentVOConverter;
        this.commentVOToCommentConverter = commentVOToCommentConverter;
        this.entryVOToEntryConverter = entryVOToEntryConverter;
        this.userVOToUserConverter = userVOToUserConverter;
    }

    @Override
    public CommentVO getOne(Long id) throws ServiceException {

        Comment comment = commentDAO.findOne(id);

        if (comment == null) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        return commentToCommentVOConverter.convert(comment);
    }

    @Override
    public List<CommentVO> getAll() {

        return commentDAO.findAll().stream()
                .map(e -> commentToCommentVOConverter.convert(e))
                .collect(Collectors.toList());
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfCommentsForEntry(int page, int limit, OrderDirection direction,
                                                             CommentVO.OrderBy orderBy, EntryVO entryVO) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Entry entry = entryVOToEntryConverter.convert(entryVO);
        Page<Comment> commentPage = commentDAO.findByEntry(pageable, entry);

        return PageableUtil.convertPage(commentPage, commentToCommentVOConverter, CommentVO.class);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfPublicCommentsForEntry(int page, int limit, OrderDirection direction,
                                                                   CommentVO.OrderBy orderBy, EntryVO entryVO) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Entry entry = entryVOToEntryConverter.convert(entryVO);
        Specifications<Comment> specifications = Specifications
                .where(CommentSpecification.IS_ENABLED);
        Page<Comment> commentPage = commentDAO.findByEntry(specifications, pageable, entry);

        return PageableUtil.convertPage(commentPage, commentToCommentVOConverter, CommentVO.class);
    }

    @Override
    public EntityPageVO<CommentVO> getPageOfCommentsForUser(int page, int limit, OrderDirection direction,
                                                            CommentVO.OrderBy orderBy, UserVO userVO) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        User user = userVOToUserConverter.convert(userVO);
        Page<Comment> commentPage = commentDAO.findByUser(pageable, user);

        return PageableUtil.convertPage(commentPage, commentToCommentVOConverter, CommentVO.class);
    }

    @Override
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

        return savedComment.getId();
    }

    @Override
    public List<Long> createBulk(List<CommentVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (CommentVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    public CommentVO updateOne(Long id, CommentVO updatedEntity) throws ServiceException {

        Comment updatedComment = commentDAO.updateOne(id, commentVOToCommentConverter.convert(updatedEntity));

        if (updatedComment == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        return commentToCommentVOConverter.convert(updatedComment);
    }

    @Override
    public List<CommentVO> updateBulk(Map<Long, CommentVO> updatedEntities) throws ServiceException {

        List<CommentVO> commentVOs = new LinkedList<>();

        Iterator<Map.Entry<Long, CommentVO>> comments = updatedEntities.entrySet().iterator();
        while (comments.hasNext()) {
            Map.Entry<Long, CommentVO> currentEntity = comments.next();
            CommentVO updatedComment = updateOne(currentEntity.getKey(), currentEntity.getValue());
            commentVOs.add(updatedComment);
        }

        return commentVOs;
    }

    @Override
    public void deleteByEntity(CommentVO entity) throws ServiceException {

        if (!commentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Comment.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {

        try {
            commentDAO.delete(id);
        } catch (IllegalArgumentException exc) {
            LOGGER.error("Error occurred during deletion", exc);
            throw new EntityNotFoundException(Comment.class, id);
        }
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!commentDAO.exists(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentDAO.disable(id);
    }

    @Override
    public void deleteLogicallyByEntity(CommentVO entity) throws ServiceException {

        if (!commentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Comment.class, entity.getId());
        }

        commentDAO.markAsDeleted(entity.getId());
    }

    @Override
    public void restoreEntity(CommentVO entity) throws ServiceException {

        if (!commentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Comment.class, entity.getId());
        }

        commentDAO.revertLogicalDeletion(entity.getId());
    }

}
