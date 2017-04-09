package hu.psprog.leaflet.service.exception;

/**
 * Directory creation exception.
 *
 * @author Peter Smith
 */
public class DirectoryCreationException extends Exception {

    public DirectoryCreationException(String message) {
        super(message);
    }

    public DirectoryCreationException(String message, Exception exception) {
        super(message, exception);
    }
}
