package hu.psprog.leaflet.web.filter.restrictions.domain;

/**
 * Supported security measures for client acceptor filter.
 * By providing one of the types below, the filter will check if the client conforms the specified security measure.
 *
 * @author Peter Smith
 */
public enum RestrictionType {

    /**
     * Client must provide a device ID.
     */
    DEVICE_ID,

    /**
     * Client must provide a Captcha token (only for specific endpoints).
     */
    CAPTCHA_TOKEN,

    /**
     * Client must provide a CSRF token (only for specific endpoints).
     */
    CSRF_TOKEN
}
