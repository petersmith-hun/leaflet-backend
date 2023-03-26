package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Entry} entities to generate a {@link Specification} that checks if
 * an entry is enabled or disabled.
 * Only applicable if the {@link EntrySearchParametersVO} object contains the enabled flag.
 *
 * @author Peter Smith
 */
@Component
public class EntryByEnabledFlagSearchStrategy implements SearchStrategy<EntrySearchParametersVO, Entry> {

    @Override
    public Specification<Entry> getSpecification(EntrySearchParametersVO searchParameters) {

        return searchParameters.getEnabled().get()
                ? EntrySpecification.IS_ENABLED
                : EntrySpecification.IS_DISABLED;
    }

    @Override
    public boolean isApplicable(EntrySearchParametersVO searchParameters) {
        return searchParameters.getEnabled().isPresent();
    }
}
