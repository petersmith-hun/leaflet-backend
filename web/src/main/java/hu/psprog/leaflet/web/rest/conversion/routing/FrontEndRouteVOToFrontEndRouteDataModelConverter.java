package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteDataModel;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link FrontEndRouteVO} to {@link FrontEndRouteDataModel}.FrontEndRouteDataModel
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteVOToFrontEndRouteDataModelConverter implements Converter<FrontEndRouteVO, FrontEndRouteDataModel> {

    @Override
    public FrontEndRouteDataModel convert(FrontEndRouteVO source) {
        return FrontEndRouteDataModel.getBuilder()
                .withRouteId(source.getRouteId())
                .withName(source.getName())
                .withUrl(source.getUrl())
                .withAuthRequirement(source.getAuthRequirement().name())
                .build();
    }
}
