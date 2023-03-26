package hu.psprog.leaflet.service.impl.search;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Implementations of this interface must provide functionality to prepare JPA {@link Specification} instances
 * (actually, instance chains) for the given entity type based on the given search parameters. These implementations
 * are more like coordinator components above the SearchStrategy implementations.
 *
 * @param <T> arbitrary type containing search parameters
 * @param <E> entity class of type {@link SerializableEntity}
 * @see hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy
 * @author Peter Smith
 */
public interface SearchHandler<T, E extends SerializableEntity> {

    /**
     * Translates the given search parameters into a JPA {@link Specification} instance.
     *
     * @param searchParameters object containing the search parameters
     * @return translated {@link Specification} instance
     */
    Specification<E> createSpecification(T searchParameters);
}
