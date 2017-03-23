package hu.psprog.leaflet.service.validation;

import java.util.Objects;

/**
 * Retrieves validator chain for given {@link Validator} instances.
 *
 * @param <T> validator of T extending {@link Validator}
 * @param <E> object type of E to execute validation on
 * @author Peter Smith
 */
public abstract class AbstractValidatorChain<T extends Validator<E>, E> {

    private final T firstValidator;

    public AbstractValidatorChain(T firstValidator) {
        this.firstValidator = firstValidator;
    }

    /**
     * Runs validator chain.
     * Validation chain runs until a validator returns false.
     * Validation chain is immediately interrupted if no validator is available in the chain, and returns false.
     *
     * @param object object to validate of type E
     * @return boolean {@code true} if given object is valid according to the validator chain, {@code false} otherwise
     */
    public boolean runChain(E object) {

        boolean valid = false;
        if (Objects.nonNull(firstValidator)) {
            valid = true;
            for (Validator<E> currentValidator = firstValidator; valid && Objects.nonNull(currentValidator); currentValidator = currentValidator.nextValidator()) {
                valid = currentValidator.isValid(object);
            }
        }

        return valid;
    }
}
