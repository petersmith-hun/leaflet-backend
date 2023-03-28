package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.CommentRepository}.
 *
 * @author Peter Smith
 */
public interface CommentDAO extends BaseDAO<Comment, Long>, SelfStatusAwareDAO<Long>, LogicallyDeletableDAO<Long> {

    Page<Comment> findAll(Specification<Comment> specification, Pageable pageable);
}
