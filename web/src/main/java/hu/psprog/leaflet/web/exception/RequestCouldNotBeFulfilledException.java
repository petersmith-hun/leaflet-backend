package hu.psprog.leaflet.web.exception;

/**
 * Should be thrown when user interaction causes exception.
 *
 * @author Peter Smith
 */
public class RequestCouldNotBeFulfilledException extends Exception {

    public RequestCouldNotBeFulfilledException() {
        super();
    }

    public RequestCouldNotBeFulfilledException(String message) {
        super(message);
    }

    public RequestCouldNotBeFulfilledException(String message, Exception exception) {
        super(message, exception);
    }

    public RequestCouldNotBeFulfilledException(Exception exception) {
        super(exception);
    }
}
