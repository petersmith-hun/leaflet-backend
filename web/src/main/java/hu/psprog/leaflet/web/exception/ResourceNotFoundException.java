package hu.psprog.leaflet.web.exception;

/**
 * Should be thrown when requested resource does not exist.
 *
 * @author Peter Smith
 */
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Exception exception) {
        super(message, exception);
    }

    public ResourceNotFoundException(Exception exception) {
        super(exception);
    }
}
