package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Comment_;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Comment} entity.
 *
 * @author Peter Smith
 */
public class CommentSpecification extends AbstractCommonSpecification {

    /**
     * Filter to list comments marked as enabled.
     */
    public static final Specification<Comment> IS_ENABLED = (root, query, builder) -> builder.equal(root.get(Comment_.enabled), true);

    /**
     * Filter to list comments marked as disabled.
     */
    public static final Specification<Comment> IS_DISABLED = (root, query, builder) -> builder.equal(root.get(Comment_.enabled), false);

    /**
     * Filter to list comments marked as logically deleted.
     */
    public static final Specification<Comment> IS_DELETED = (root, query, builder) -> builder.equal(root.get(Comment_.deleted), true);

    /**
     * Filter to list comments marked as not logically deleted.
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

    /**
     * Filter to list comments by owner user.
     *
     * @param user owner {@link User} entity.
     * @return filter to list of comments owned by given user
     */
    public static Specification<Comment> isOwnedByUser(User user) {
        return (root, query, builder) -> builder.equal(root.get(Comment_.user), user);
    }

    /**
     * {@link Specification} implementation to check if a comment's content contains the given expression.
     * The provided expression will be modified to include the required wildcard (%) for SQL LIKE queries.
     *
     * @param expression expression as {@link String}
     * @return built {@link Specification}
     */
    public static Specification<Comment> containsExpression(String expression) {
        String likeExpression = createLikeExpression(expression);
        return (root, query, builder) -> builder.like(root.get(Comment_.content), likeExpression);
    }
}
