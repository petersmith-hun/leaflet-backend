package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.persistence.entity.Attachment_;
import hu.psprog.leaflet.persistence.entity.Entry;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Attachment} entity.
 *
 * @author Peter Smith
 */
public class AttachmentSpecification {

    /**
     * Filter to list enabled attachments.
     */
    public static Specification<Attachment> isEnabled = (root, query, builder) -> builder.equal(root.get(Attachment_.enabled), true);

    /**
     * Filter to list attachments by owner entry.
     *
     * @param entry owner {@link Entry} entity.
     * @return filter to list of attachments owned by given entry
     */
    public static Specification<Attachment> isOwnedByEntry(Entry entry) {
        return (root, query, builder) -> builder.equal(root.get(Attachment_.entry), entry);
    }
}
