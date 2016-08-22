package hu.psprog.leaflet.service.common;

/**
 * Run level types.
 *
 * @author Peter Smith
 */
public enum RunLevel {

    /**
     * Production mode - logging is on INFO level.
     */
    PRODUCTION,

    /**
     * Maintenance mode - logging is on DEBUG level (default).
     */
    MAINTENANCE,

    /**
     * Initialization mode - should ONLY be used for the first time running the application.
     * Lets anonymously create the first administrator user.
     */
    INIT;
}
