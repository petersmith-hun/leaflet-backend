package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * DAO implementation for {@link CommentRepository}.
 *
 * @author Peter Smith
 */
@Component
public class CommentDAOImpl extends SelfStatusAwareDAOImpl<Comment, Long> implements CommentDAO {

    @Autowired
    public CommentDAOImpl(final CommentRepository commentRepository) {
        super(commentRepository);
    }

    @Override
    public List<Comment> findByEntry(Pageable pageable, Entry entry) {
        return ((CommentRepository) jpaRepository).findByEntry(pageable, entry);
    }

    @Override
    public List<Comment> findByUser(Pageable pageable,  User user) {
        return ((CommentRepository) jpaRepository).findByUser(pageable, user);
    }

    @Override
    public Comment updateOne(Long id, Comment updatedEntity) {

        Comment currentComment = jpaRepository.getOne(id);
        if (currentComment != null) {
            currentComment.setContent(updatedEntity.getContent());
            currentComment.setLastModified(new Date());
        }

        return currentComment;
    }
}
