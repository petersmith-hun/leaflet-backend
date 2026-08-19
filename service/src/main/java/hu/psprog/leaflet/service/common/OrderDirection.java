package hu.psprog.leaflet.service.common;

import lombok.Getter;
import org.springframework.data.domain.Sort;

/**
 * Supported order directions.
 *
 * @author Peter Smith
 */
@Getter
public enum OrderDirection {

    ASC(Sort.Direction.ASC),
    DESC(Sort.Direction.DESC);

    private final Sort.Direction direction;

    OrderDirection(Sort.Direction direction) {
        this.direction = direction;
    }

}
