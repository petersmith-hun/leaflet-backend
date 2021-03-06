package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.Document_;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Document} entity.
 *
 * @author Peter Smith
 */
public class DocumentSpecification {

    /**
     * Filter to list documents marked as public.
     */
    public static final Specification<Document> IS_ENABLED = (root, query, builder) -> builder.equal(root.get(Document_.enabled), true);

    private DocumentSpecification() {}
}
