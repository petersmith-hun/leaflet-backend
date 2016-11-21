package hu.psprog.leaflet.service.exception;

/**
 * Persistence constraint violation wrapper exception.
 *
 * @author Peter Smith
 */
public class ConstraintViolationException extends ServiceException {

    private static final String MESSAGE = "Primary/foreign key violation exception occurred. See details: ";

    public ConstraintViolationException(Exception exception) {
        super(MESSAGE, exception);
    }
}
