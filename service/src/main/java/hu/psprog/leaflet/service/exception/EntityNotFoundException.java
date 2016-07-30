package hu.psprog.leaflet.service.exception;

import java.io.Serializable;

/**
 * Exception should be thrown when query for specified entity returns null.
 *
 * @author Peter Smith
 */
public class EntityNotFoundException extends ServiceException {

    private static final String MESSAGE_PATTERN = "Entity of type [%s] identified by [%s] not found.";

    public EntityNotFoundException(Class<?> entityClass, Serializable identifier) {
        super(String.format(MESSAGE_PATTERN, entityClass.getName(), identifier.toString()));
    }
}
