package hu.psprog.leaflet.persistence.entity;

/**
 * Possible front-end route types.
 *
 * @author Peter Smith
 */
public enum FrontEndRouteType {

    /**
     * Route item is part of the header menu.
     */
    HEADER_MENU,

    /**
     * Route item is part of the footer menu.
     */
    FOOTER_MENU,

    /**
     * Route item is a standalone route, not part of any menus.
     */
    STANDALONE,

    /**
     * Route item is a standalone dynamic mask for a group of entry routes.
     */
    ENTRY_ROUTE_MASK,

    /**
     * Route item is a standalone dynamic mask for a group of category routes.
     */
    CATEGORY_ROUTE_MASK,

    /**
     * Route item is a standalone dynamic mask for a group of tag routes.
     */
    TAG_ROUTE_MASK
}
