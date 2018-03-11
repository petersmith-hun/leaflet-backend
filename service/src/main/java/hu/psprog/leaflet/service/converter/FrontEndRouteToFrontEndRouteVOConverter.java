package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link FrontEndRoute} entity to {@link FrontEndRouteVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteToFrontEndRouteVOConverter implements Converter<FrontEndRoute, FrontEndRouteVO> {

    @Override
    public FrontEndRouteVO convert(FrontEndRoute source) {

        return FrontEndRouteVO.getBuilder()
                .withName(source.getName())
                .withUrl(source.getUrl())
                .withSequenceNumber(source.getSequenceNumber())
                .withType(source.getType())
                .withEnabled(source.isEnabled())
                .build();
    }
}
