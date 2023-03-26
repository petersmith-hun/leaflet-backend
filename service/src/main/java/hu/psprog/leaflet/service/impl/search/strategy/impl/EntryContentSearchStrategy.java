package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Entry} entities to generate a {@link Specification} that checks if
 * an entry contains a specific expression.
 * Only applicable if the {@link EntrySearchParametersVO} object contains the expected expression, and it is at least 3
 * characters long (inclusive).
 *
 * @author Peter Smith
 */
@Component
public class EntryContentSearchStrategy implements SearchStrategy<EntrySearchParametersVO, Entry> {

    private static final int MINIMUM_CONTENT_LENGTH = 3;

    @Override
    public Specification<Entry> getSpecification(EntrySearchParametersVO searchParameters) {
        return EntrySpecification.containsExpression(searchParameters.getContent().get());
    }

    @Override
    public boolean isApplicable(EntrySearchParametersVO searchParameters) {

        return searchParameters.getContent()
                .filter(content -> content.length() >= MINIMUM_CONTENT_LENGTH)
                .isPresent();
    }
}
