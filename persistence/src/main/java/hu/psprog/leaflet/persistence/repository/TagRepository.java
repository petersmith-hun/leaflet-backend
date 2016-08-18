package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Tag repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Returns {@link List} of {@link Tag} objects associated with given {@link Entry}.
     *
     * @param entry {@link Entry} object to retrieve tags associated with
     * @return List of Tag objects or {@code null} if no Tag found
     */
    //public List<Tag> findByEntry(Entry entry);
}
