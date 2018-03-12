package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.request.routing.FrontEndRouteUpdateRequestModel;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link FrontEndRouteUpdateRequestModel} to {@link FrontEndRouteVO}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteUpdateRequestModelToFrontEndRouteVOConverter implements Converter<FrontEndRouteUpdateRequestModel, FrontEndRouteVO> {

    private static final boolean ENABLED_BY_DEFAULT = true;

    @Override
    public FrontEndRouteVO convert(FrontEndRouteUpdateRequestModel source) {

        return FrontEndRouteVO.getBuilder()
                .withRouteId(source.getRouteId())
                .withName(source.getName())
                .withUrl(source.getUrl())
                .withSequenceNumber(source.getSequenceNumber())
                .withType(FrontEndRouteType.valueOf(source.getType()))
                .withEnabled(ENABLED_BY_DEFAULT)
                .build();
    }
}
