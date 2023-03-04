package hu.psprog.leaflet.service.common;

import org.springframework.data.domain.Sort;

/**
 * Order directions page {@link hu.psprog.leaflet.service.crud.PageableService} services.
 *
 * @author Peter Smith
 */
public enum OrderDirection {

    ASC(Sort.Direction.ASC),
    DESC(Sort.Direction.DESC);

    private final Sort.Direction direction;

    OrderDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public Sort.Direction getDirection() {
        return direction;
    }
}
