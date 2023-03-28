package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;

/**
 * DAO implementation for {@link CommentRepository}.
 *
 * @author Peter Smith
 */
@Component
public class CommentDAOImpl extends LogicallyDeletableSelfStatusAwareDAOImpl<Comment, Long> implements CommentDAO {

    @Autowired
    public CommentDAOImpl(final CommentRepository commentRepository, JpaContext jpaContext) {
        super(commentRepository, jpaContext);
    }

    @Override
    public Page<Comment> findAll(Specification<Comment> specification, Pageable pageable) {
        return ((CommentRepository) jpaRepository).findAll(specification, pageable);
    }

    @Override
    protected void doUpdate(Comment currentEntity, Comment updatedEntity) {
        currentEntity.setContent(updatedEntity.getContent());
    }
}
