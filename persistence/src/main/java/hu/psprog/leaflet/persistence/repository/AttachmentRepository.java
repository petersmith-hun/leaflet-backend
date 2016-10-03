package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.persistence.entity.Entry;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Attachment repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, JpaSpecificationExecutor<Attachment> {

    /**
     * Returns {@link List} of {@link Attachment} objects associated with given {@link Entry}.
     *
     * @param entry {@link Entry} object to retrieve attachments associated with
     * @return List of Attachment objects or {@code null} if no Attachment found
     */
    public List<Attachment> findByEntry(Entry entry);

    /**
     * Returns {@link List} of {@link Attachment} objects associated with given {@link Entry}.
     *
     * @param specification filter specification
     * @param entry {@link Entry} object to retrieve attachments associated with
     * @return List of Attachment objects or {@code null} if no Attachment found
     */
    public List<Attachment> findByEntry(Specification<Entry> specification, Entry entry);
}
