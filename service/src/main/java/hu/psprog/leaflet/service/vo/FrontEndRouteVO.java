package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Value object for {@link FrontEndRoute} entity.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class FrontEndRouteVO extends SelfStatusAwareIdentifiableVO<Long, FrontEndRoute> {

    private final String routeId;
    private final String name;
    private final String url;
    private final Integer sequenceNumber;
    private final FrontEndRouteType type;
    private final FrontEndRouteAuthRequirement authRequirement;
}
