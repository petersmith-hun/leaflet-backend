package hu.psprog.leaflet.service.validation;

/**
 * Validates retrieved information to decide whether it is capable of further processing or usage.
 * Validator instances can be chained.
 *
 * @param <T> type to execute validation on
 * @author Peter Smith
 */
public interface Validator<T> {

    /**
     * Decides if given object is "valid".
     *
     * @param object object to validate
     * @return boolean {@code true} if object is valid, {@code false} otherwise
     */
    boolean isValid(T object);

    /**
     * Returns next validator if there's any in the chain.
     *
     * @return next validator or {@code null} if there's no more validator items in the current chain
     */
    Validator<T> nextValidator();
}
