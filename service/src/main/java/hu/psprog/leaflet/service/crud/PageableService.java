package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.vo.BaseVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;

/**
 * Pageable service interface.
 *
 * @param <T> {@link BaseVO} type of entity
 * @param <O> enumeration of fields the entity can be ordered by
 */
public interface PageableService<T extends BaseVO, O extends Enum> {

    public EntityPageVO<T> getEntityPage(int page, int limit, OrderDirection direction, O orderBy);
}
