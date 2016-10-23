package hu.psprog.leaflet.web.exception;

/**
 * Base exception for authentication errors.
 *
 * @author Peter Smith
 */
public class AuthenticationFailureException extends Exception {

    public AuthenticationFailureException(String message) {
        super(message);
    }
}
