package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.Entry_;
import hu.psprog.leaflet.persistence.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link Entry} entity.
 *
 * @author Peter Smith
 */
public class EntrySpecification {

    private static final String LIKE_EXPRESSION_PATTERN = "%%%s%%";
    private static final char CHAR_SPACE = ' ';
    private static final char CHAR_PERCENTAGE = '%';

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

    public static Specification<Entry> isUnderTag(Tag tag) {
        return (root, query, builder) -> builder.isMember(tag, root.get(Entry_.tags));
    }

    public static Specification<Entry> containsExpression(String expression) {
        String likeExpression = createLikeExpression(expression);
        return (root, query, builder) -> builder.or(
                builder.like(root.get(Entry_.title), likeExpression),
                builder.like(root.get(Entry_.prologue), likeExpression),
                builder.like(root.get(Entry_.rawContent), likeExpression));
    }

    private static String createLikeExpression(String originalExpression) {
        return String.format(LIKE_EXPRESSION_PATTERN, String.valueOf(originalExpression).replace(CHAR_SPACE, CHAR_PERCENTAGE));
    }
}
