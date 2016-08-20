package hu.psprog.leaflet.service.exception;

/**
 * Exception to be thrown when an entity can not be created.
 *
 * @author Peter Smith
 */
public class EntityCreationException extends ServiceException {

    private static final String MESSAGE_PATTERN = "Entity of type [%s] could not be created";

    public EntityCreationException(Class<?> entityClass) {
        super(String.format(MESSAGE_PATTERN, entityClass.getName()));
    }
}
