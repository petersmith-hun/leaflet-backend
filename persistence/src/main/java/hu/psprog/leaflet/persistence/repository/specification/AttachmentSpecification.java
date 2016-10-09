package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.persistence.entity.Attachment_;
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
}
