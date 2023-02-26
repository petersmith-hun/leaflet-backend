package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Entry;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Entry repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface EntryRepository extends SelfStatusAwareJpaRepository<Entry, Long>, JpaSpecificationExecutor<Entry> {

    /**
     * Returns {@link Entry} object identified by given link.
     *
     * @param link entry link
     * @return Entry object identified by given link or {@code null} if no Entry found
     */
    Entry findByLink(String link);
}
