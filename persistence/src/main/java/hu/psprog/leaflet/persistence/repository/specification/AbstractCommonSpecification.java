package hu.psprog.leaflet.persistence.repository.specification;

/**
 * Filter specification support methods.
 *
 * @author Peter Smith
 */
abstract class AbstractCommonSpecification {

    private static final String LIKE_EXPRESSION_PATTERN = "%%%s%%";
    private static final char CHAR_SPACE = ' ';
    private static final char CHAR_PERCENTAGE = '%';

    protected static String createLikeExpression(String originalExpression) {
        return String.format(LIKE_EXPRESSION_PATTERN, String.valueOf(originalExpression).replace(CHAR_SPACE, CHAR_PERCENTAGE));
    }
}
