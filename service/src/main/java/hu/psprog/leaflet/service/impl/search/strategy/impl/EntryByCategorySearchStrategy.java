package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Entry} entities to generate a {@link Specification} that checks if
 * an entry is created under the given category.
 * Only applicable if the {@link EntrySearchParametersVO} object contains a category ID.
 *
 * @author Peter Smith
 */
@Component
public class EntryByCategorySearchStrategy implements SearchStrategy<EntrySearchParametersVO, Entry> {

    @Override
    public Specification<Entry> getSpecification(EntrySearchParametersVO searchParameters) {

        var category = new Category();
        searchParameters.getCategoryID().ifPresent(category::setId);

        return EntrySpecification.isUnderCategory(category);
    }

    @Override
    public boolean isApplicable(EntrySearchParametersVO searchParameters) {
        return searchParameters.getCategoryID().isPresent();
    }
}
