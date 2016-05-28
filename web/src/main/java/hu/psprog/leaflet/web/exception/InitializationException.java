package hu.psprog.leaflet.web.exception;

public class InitializationException extends Exception {

    public InitializationException() {
        super();
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Exception exception) {
        super(message, exception);
    }

    public InitializationException(Exception exception) {
        super(exception);
    }
}
