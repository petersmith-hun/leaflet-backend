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
    public static final Specification<Comment> IS_ENABLED = (root, query, builder) -> builder.equal(root.get(Comment_.enabled), true);

    /**
     * Filter to list comments not marked as logically deleted.
     */
    public static final Specification<Comment> IS_NOT_DELETED = (root, query, builder) -> builder.equal(root.get(Comment_.deleted), false);

    private CommentSpecification() {}

    /**
     * Filter to list comments by owner entry.
     *
     * @param entry owner {@link Entry} entity.
     * @return filter to list of comments owned by given entry
     */
    public static Specification<Comment> isOwnedByEntry(Entry entry) {
        return (root, query, builder) -> builder.equal(root.get(Comment_.entry), entry);
    }
}
