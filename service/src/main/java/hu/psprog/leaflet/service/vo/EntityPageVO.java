package hu.psprog.leaflet.service.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Pagination information wrapper.
 *
 * @author Peter Smith
 */
@Data
@Builder(builderMethodName = "getBuilder", setterPrefix = "with")
public class EntityPageVO<T extends BaseVO> implements Serializable {

    private final long entityCount;
    private final int pageCount;
    private final int pageNumber;
    private final int pageSize;
    private final int entityCountOnPage;
    private final List<T> entitiesOnPage;
    private final boolean first;
    private final boolean last;

    @Getter(AccessLevel.NONE)
    private final boolean hasNext;

    @Getter(AccessLevel.NONE)
    private final boolean hasPrevious;

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }
}
