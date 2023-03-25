package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.request.common.OrderBy;
import hu.psprog.leaflet.api.rest.request.common.OrderDirection;
import hu.psprog.leaflet.api.rest.request.entry.EntryInitialStatus;
import hu.psprog.leaflet.api.rest.request.entry.EntrySearchParameters;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Converts an {@link EntrySearchParameters} object to an {@link EntrySearchParametersVO} object.
 * Sets the following default pagination parameters if overrides are not present:
 *  - A page size of 10;
 *  - And ascending sorting by creation date.
 *
 * @author Peter Smith
 */
@Component
public class EntrySearchParametersConverter implements Converter<EntrySearchParameters, EntrySearchParametersVO> {

    private static final int DEFAULT_LIMIT = 10;
    private static final EntryVO.OrderBy DEFAULT_ORDER_BY = EntryVO.OrderBy.CREATED;
    private static final hu.psprog.leaflet.service.common.OrderDirection DEFAULT_ORDER_DIRECTION = hu.psprog.leaflet.service.common.OrderDirection.ASC;

    @Override
    public EntrySearchParametersVO convert(EntrySearchParameters source) {
        
        return EntrySearchParametersVO.builder()
                .categoryID(source.getCategoryID())
                .enabled(source.getEnabled())
                .status(mapStatus(source))
                .content(source.getContent())
                .page(source.getPage())
                .limit(mapLimit(source))
                .orderBy(mapOrderBy(source))
                .orderDirection(mapOrderDirection(source))
                .build();
    }

    private Optional<EntryStatus> mapStatus(EntrySearchParameters source) {

        return source.getStatus()
                .map(EntryInitialStatus::name)
                .map(EntryStatus::valueOf);
    }

    private Integer mapLimit(EntrySearchParameters source) {

        return source.getLimit()
                .orElse(DEFAULT_LIMIT);
    }

    private EntryVO.OrderBy mapOrderBy(EntrySearchParameters source) {
        return source.getOrderBy()
                .map(OrderBy.Entry::name)
                .map(EntryVO.OrderBy::valueOf)
                .orElse(DEFAULT_ORDER_BY);
    }

    private hu.psprog.leaflet.service.common.OrderDirection mapOrderDirection(EntrySearchParameters source) {

        return source.getOrderDirection()
                .map(OrderDirection::name)
                .map(hu.psprog.leaflet.service.common.OrderDirection::valueOf)
                .orElse(DEFAULT_ORDER_DIRECTION);
    }
}
