package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Blog entry repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface BlogEntryRepository extends JpaRepository<Entry, Long> {
}
