package hu.psprog.leaflet.service.exception;

/**
 * File not found exception.
 *
 * @author Peter Smith
 */
public class FileNotFoundException extends Exception {

    public FileNotFoundException(String message, Exception exception) {
        super(message, exception);
    }
}
