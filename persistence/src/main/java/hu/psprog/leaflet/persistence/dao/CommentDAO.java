package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.CommentRepository}.
 *
 * @author Peter Smith
 */
public interface CommentDAO extends BaseDAO<Comment, Long>, SelfStatusAwareDAO<Long>, LogicallyDeletableDAO<Long> {

    Page<Comment> findByEntry(Pageable pageable, Entry entry);

    Page<Comment> findByEntry(Specification<Comment> specification, Pageable pageable, Entry entry);

    Page<Comment> findByUser(Pageable pageable,  User user);
}
