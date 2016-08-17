package hu.psprog.leaflet.service.exception;

/**
 * @author Peter Smith
 */
public class EntityCreationException extends ServiceException {

    private static final String MESSAGE_PATTERN = "Entity of type [%s] could not be created";

    public EntityCreationException(Class<?> entityClass) {
        super(String.format(MESSAGE_PATTERN, entityClass.getName()));
    }
}
