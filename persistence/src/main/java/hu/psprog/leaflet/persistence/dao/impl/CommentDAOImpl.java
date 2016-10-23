package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.CommentRepository;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    public Page<Comment> findByEntry(Pageable pageable, Entry entry) {
        return ((CommentRepository) jpaRepository).findByEntry(pageable, entry);
    }

    @Override
    public Page<Comment> findByEntry(Specification<Comment> specification, Pageable pageable, Entry entry) {
        return ((CommentRepository) jpaRepository).findAll(Specifications.where(specification)
                .and(CommentSpecification.isOwnedByEntry(entry)), pageable);
    }

    @Override
    public Page<Comment> findByUser(Pageable pageable, User user) {
        return ((CommentRepository) jpaRepository).findByUser(pageable, user);
    }

    @Transactional
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
