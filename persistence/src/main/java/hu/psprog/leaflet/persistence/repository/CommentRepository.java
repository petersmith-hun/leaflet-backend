package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Comment repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Returns {@link List} of {@link Comment} objects associated with given {@link Entry}.
     *
     * @param entry {@link Entry} object to retrieve comments associated with
     * @return List of Comment objects or {@code null} if no Comment found
     */
    public List<Comment> findByEntry(Entry entry);

    /**
     * Returns {@link List} of {@link Comment} objects associated with given {@link User}.
     *
     * @param user {@link User} object to retrieve comments associated with
     * @return List of Comment objects or {@code null} if no Comment found
     */
    public List<Comment> findByUser(User user);
}
