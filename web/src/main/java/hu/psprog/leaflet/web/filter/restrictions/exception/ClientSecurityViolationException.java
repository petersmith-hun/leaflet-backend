package hu.psprog.leaflet.web.filter.restrictions.exception;

/**
 * Exception to be thrown when a client violates some of its required security measures.
 *
 * @author Peter Smith
 */
public class ClientSecurityViolationException extends SecurityRestrictionViolationException {

    private static final String SECURITY_VIOLATION_MESSAGE = "Client violated a security restriction.";

    public ClientSecurityViolationException() {
        super(SECURITY_VIOLATION_MESSAGE);
    }
}
