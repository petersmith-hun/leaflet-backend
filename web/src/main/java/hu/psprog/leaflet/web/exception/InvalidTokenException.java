package hu.psprog.leaflet.web.exception;

/**
 * Exception to be thrown when token expires, or is invalid (structural error, invalid signature, etc.).
 *
 * @author Peter Smith
 */
public class InvalidTokenException extends AuthenticationFailureException {

    private static final String INVALID_TOKEN = "You need to re-login to continue.";

    public InvalidTokenException() {
        super(INVALID_TOKEN);
    }
}
