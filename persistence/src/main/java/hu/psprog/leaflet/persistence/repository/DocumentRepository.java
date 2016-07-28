package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Document repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Returns {@link List} of {@link Document} objects associated with given {@link User}.
     *
     * @param user {@link User} object to retrieve documents associated with
     * @return List of Document objects or {@code null} if no Document found
     */
    public List<Document> findByUser(User user);
}
