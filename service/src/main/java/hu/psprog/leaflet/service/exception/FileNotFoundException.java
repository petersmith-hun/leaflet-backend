package hu.psprog.leaflet.service.exception;

/**
 * File not found exception.
 *
 * @author Peter Smith
 */
public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String message, Exception exception) {
        super(message, exception);
    }
}
