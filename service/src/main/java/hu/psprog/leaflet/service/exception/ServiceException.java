package hu.psprog.leaflet.service.exception;

/**
 * Base service layer related exception.
 *
 * @author Peter Smith
 */
public class ServiceException extends Exception {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Exception exception) {
        super(exception);
    }

    public ServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
