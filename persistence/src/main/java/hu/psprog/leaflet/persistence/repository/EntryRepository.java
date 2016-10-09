package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Entry findByLink(String link);

    /**
     * Returns {@link List} of {@link Entry} objects associated with given {@link User}.
     *
     * @param user {@link User} object to retrieve entries associated with
     * @return List of Entry objects or {@code null} if no Entry found
     */
    public List<Entry> findByUser(User user);

    /**
     * Returns {@link List} of {@link Entry} objects associated with given {@link Category}.
     *
     * @param category {@link Category} object to retrieve entries associated with
     * @return List of Entry objects or {@code null} if no Entry found
     */
    public List<Entry> findByCategory(Category category);

    /**
     * Returns {@link List} of {@link Entry} objects associated with given {@link Tag}.
     *
     * @param tag {@link Tag} object to retrieve entries associated with
     * @return List of Entry objects or {@code null} if no Entry found
     */
    public List<Entry> findByTags(Tag tag);
}
