package hu.psprog.leaflet.web.annotation;

/**
 * Enumeration for response fill mode types.
 */
public enum ResponseFillMode {

    /**
     * All applicable response fillers will be applied.
     */
    ALL,

    /**
     * Only those response fillers will be applied, that are also required for an AJAX request.
     */
    AJAX
}
