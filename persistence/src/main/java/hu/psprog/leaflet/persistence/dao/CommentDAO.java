package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.CommentRepository}.
 *
 * @author Peter Smith
 */
public interface CommentDAO extends BaseDAO<Comment, Long>, SelfStatusAwareDAO<Long> {

    public List<Comment> findByEntry(Pageable pageable, Entry entry);

    public List<Comment> findByUser(Pageable pageable,  User user);
}
