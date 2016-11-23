package hu.psprog.leaflet.web.rest.filler;

/**
 * Request parameter names.
 *
 * @author Peter Smith
 */
public class RequestParameter {

    public static final String PAGINATION_ENTITY_COUNT = "ENTITY_COUNT";
    public static final String PAGINATION_ENTITY_COUNT_ON_PAGE = "ENTITY_COUNT_ON_PAGE";
    public static final String PAGINATION_PAGE_COUNT = "PAGE_COUNT";
    public static final String PAGINATION_PAGE_NUMBER = "PAGE_NUMBER";
    public static final String PAGINATION_IS_FIRST = "IS_FIRST";
    public static final String PAGINATION_IS_LAST = "IS_LAST";
    public static final String PAGINATION_HAS_NEXT = "HAS_NEXT";
    public static final String PAGINATION_HAS_PREVIOUS = "HAS_PREVIOUS";

    public static final String SEO_PAGE_TITLE = "PAGE_TITLE";
    public static final String SEO_META_TITLE = "META_TITLE";
    public static final String SEO_META_DESCRIPTION = "META_DESCRIPTION";
    public static final String SEO_META_KEYWORDS = "META_KEYWORDS";

    private RequestParameter() {
        // prevent initialization
    }
}
