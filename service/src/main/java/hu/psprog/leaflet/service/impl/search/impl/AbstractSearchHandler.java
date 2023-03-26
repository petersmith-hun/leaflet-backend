package hu.psprog.leaflet.service.impl.search.impl;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import hu.psprog.leaflet.service.impl.search.SearchHandler;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.function.Supplier;

/**
 * Abstract common {@link SearchHandler} implementation.
 * Executes the applicable {@link SearchStrategy} implementations and chains them together using "and" operation.
 * Concrete implementations must provide a base (identity) specification, that can be applied in the search query if no
 * search strategy could provide a {@link Specification}.
 *
 * @param <T> arbitrary type containing search parameters
 * @param <E> entity class of type {@link SerializableEntity}
 * @author Peter Smith
 */
abstract class AbstractSearchHandler<T, E extends SerializableEntity> implements SearchHandler<T, E> {

    private final List<SearchStrategy<T, E>> searchStrategies;

    protected AbstractSearchHandler(List<SearchStrategy<T, E>> searchStrategies) {
        this.searchStrategies = searchStrategies;
    }

    @Override
    public Specification<E> createSpecification(T searchParameters) {

        return searchStrategies.stream()
                .filter(strategy -> strategy.isApplicable(searchParameters))
                .map(strategy -> strategy.getSpecification(searchParameters))
                .reduce(baseSpecificationSupplier().get(), Specification::and);
    }

    /**
     * Supplies the base specification of the concrete search handler implementation.
     *
     * @return base specification wrapped as {@link Supplier}
     */
    protected abstract Supplier<Specification<E>> baseSpecificationSupplier();
}
