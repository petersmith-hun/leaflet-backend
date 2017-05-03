package hu.psprog.leaflet.service.exception;

/**
 * File upload failure.
 *
 * @author Peter Smith
 */
public class FileUploadException extends RuntimeException {

    public FileUploadException(String message, Exception exception) {
        super(message, exception);
    }
}
