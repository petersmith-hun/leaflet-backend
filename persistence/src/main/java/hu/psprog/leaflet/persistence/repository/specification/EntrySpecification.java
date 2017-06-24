package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.Entry_;
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
    public static final Specification<Entry> IS_PUBLIC = (root, query, builder) -> builder.equal(root.get(Entry_.status), EntryStatus.PUBLIC);

    /**
     * Filter to list entries marked as enabled.
     */
    public static final Specification<Entry> IS_ENABLED = (root, query, builder) -> builder.equal(root.get(Entry_.enabled), true);

    private EntrySpecification() {}

    public static Specification<Entry> isUnderCategory(Category category) {
        return (root, query, builder) -> builder.equal(root.get(Entry_.category), category);
    }
}
