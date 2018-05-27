package hu.psprog.leaflet.web.filter.restrictions.exception;

/**
 * Common base exception for security exceptions thrown by client acceptor filter.
 *
 * @author Peter Smith
 */
public abstract class SecurityRestrictionViolationException extends RuntimeException {

    SecurityRestrictionViolationException(String message) {
        super(message);
    }
}
