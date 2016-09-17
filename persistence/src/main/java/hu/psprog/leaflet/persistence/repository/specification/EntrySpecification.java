package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.metamodel.Entry_;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Entry} entity.
 *
 * @author Peter Smith
 */
public class EntrySpecification {

    /**
     * Filter to list entries marked as public.
     */
    public static Specification<Entry> isPublic = (root, query, builder) -> builder.equal(root.get(Entry_.status), EntryStatus.PUBLIC);
}
