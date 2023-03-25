package hu.psprog.leaflet.service.impl.search.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

/**
 * {@link AbstractSearchHandler} implementation for entry search requests.
 *
 * @author Peter Smith
 */
@Component
public class EntrySearchHandler extends AbstractSearchHandler<EntrySearchParametersVO, Entry> {

    @Autowired
    public EntrySearchHandler(List<SearchStrategy<EntrySearchParametersVO, Entry>> searchStrategies) {
        super(searchStrategies);
    }

    @Override
    protected Supplier<Specification<Entry>> baseSpecificationSupplier() {
        return () -> Specification.where(null);
    }
}
