package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Category_;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Category} entity.
 *
 * @author Peter Smith
 */
public class CategorySpecification {

    /**
     * Filter to list categories marked as enabled.
     */
    public static final Specification<Category> IS_ENABLED = (root, query, builder) -> builder.equal(root.get(Category_.enabled), true);

    private CategorySpecification() {}
}
