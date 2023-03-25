package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Entry} entities to generate a {@link Specification} that checks if
 * the given tag is attached to the entry.
 * Only applicable if the {@link EntrySearchParametersVO} object contains a tag ID.
 *
 * @author Peter Smith
 */
@Component
public class EntryByTagSearchStrategy implements SearchStrategy<EntrySearchParametersVO, Entry> {

    @Override
    public Specification<Entry> getSpecification(EntrySearchParametersVO searchParameters) {

        var tag = new Tag();
        searchParameters.getTagID().ifPresent(tag::setId);

        return EntrySpecification.isUnderTag(tag);
    }

    @Override
    public boolean isApplicable(EntrySearchParametersVO searchParameters) {
        return searchParameters.getTagID().isPresent();
    }
}
