package hu.psprog.leaflet.web.filter.restrictions.exception;

/**
 * Exception to be thrown when a client does not specify its client ID in its request.
 *
 * @author Peter Smith
 */
public class MissingClientIDHeaderException extends SecurityRestrictionViolationException {

    private static final String CLIENT_CANNOT_BE_IDENTIFIED = "Client cannot be identified.";

    public MissingClientIDHeaderException() {
        super(CLIENT_CANNOT_BE_IDENTIFIED);
    }
}
