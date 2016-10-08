package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Comment_;
import hu.psprog.leaflet.persistence.entity.Entry;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Comment} entity.
 *
 * @author Peter Smith
 */
public class CommentSpecification {

    /**
     * Filter to list comments marked as enabled.
     */
    public static Specification<Comment> isEnabled = (root, query, builder) -> builder.equal(root.get(Comment_.enabled), true);

    public static Specification<Comment> isOwnedByEntry(Entry entry) {
        return (root, query, builder) -> builder.equal(root.get(Comment_.entry), entry);
    }
}
