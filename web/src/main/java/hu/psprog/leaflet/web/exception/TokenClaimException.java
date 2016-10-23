package hu.psprog.leaflet.web.exception;

/**
 * Exception to be thrown when given user credentials are invalid during token claiming (not existing user, bad password, etc.).
 *
 * @author Peter Smith
 */
public class TokenClaimException extends AuthenticationFailureException {

    private static final String TOKEN_CLAIM = "Your credentials are invalid. Please check your email address and/or password.";

    public TokenClaimException() {
        super(TOKEN_CLAIM);
    }
}
