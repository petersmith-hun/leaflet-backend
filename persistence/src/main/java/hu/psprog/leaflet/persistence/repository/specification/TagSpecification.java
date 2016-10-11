package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.Tag_;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Tag} entity.
 *
 * @author Peter Smith
 */
public class TagSpecification {

    public static Specification<Tag> isEnabled = (root, query, builder) -> builder.equal(root.get(Tag_.enabled), true);
}
