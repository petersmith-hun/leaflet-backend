package hu.psprog.leaflet.service.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * Exception to throw when current user cannot be re-authenticated.
 *
 * @author Peter Smith
 */
public class ReAuthenticationFailureException extends AccessDeniedException {

    private static final String RE_AUTH_FAILURE_MESSAGE = "Re-authentication failed - provided password is incorrect.";

    public ReAuthenticationFailureException(Throwable e) {
        super(RE_AUTH_FAILURE_MESSAGE, e);
    }
}
