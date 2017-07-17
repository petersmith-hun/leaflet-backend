package hu.psprog.leaflet.service.exception;

/**
 * Exception to be thrown when user initialization can not be performed (already initialized or application is not in INIT mode.
 *
 * @author Peter Smith
 */
public class UserInitializationException extends ServiceException {

    public UserInitializationException(String message) {
        super(message);
    }
}
