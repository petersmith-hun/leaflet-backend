package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link FrontEndRouteVO} value object to {@link FrontEndRoute} entity.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteVOToFrontEndRouteConverter implements Converter<FrontEndRouteVO, FrontEndRoute> {

    @Override
    public FrontEndRoute convert(FrontEndRouteVO source) {

        return FrontEndRoute.getBuilder()
                .withName(source.getName())
                .withUrl(source.getUrl())
                .withSequenceNumber(source.getSequenceNumber())
                .withType(source.getType())
                .withEnabled(source.isEnabled())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withId(source.getId())
                .build();
    }
}
