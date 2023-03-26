package hu.psprog.leaflet.service.impl.search.strategy;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Implementations of this interface must provide functionality to generate a single segment (i.e. a single condition)
 * of a JPA {@link Specification} chain. Each implementation should define under what condition they can be executed and
 * the actual operation (i.e. translating that single condition into a {@link Specification} instance). In case the
 * strategy is applicable, it must generate a valid {@link Specification}.
 *
 * @param <T> arbitrary type containing search parameters
 * @param <E> entity class of type {@link SerializableEntity}
 * @author Peter Smith
 */
public interface SearchStrategy<T, E extends SerializableEntity> {

    /**
     * Translates a single search condition into a {@link Specification}.
     *
     * @param searchParameters object containing the search parameters
     * @return translated {@link Specification} instance
     */
    Specification<E> getSpecification(T searchParameters);

    /**
     * Checks if this strategy is applicable to the search parameters. E.g., in case of a complex search parameter
     * object, the implementation will check if a specific parameter exists, or has a specific value.
     *
     * @param searchParameters object containing the search parameters
     * @return {@code true} is this strategy is applicable to the given search parameter, {@code false} otherwise
     */
    boolean isApplicable(T searchParameters);
}
