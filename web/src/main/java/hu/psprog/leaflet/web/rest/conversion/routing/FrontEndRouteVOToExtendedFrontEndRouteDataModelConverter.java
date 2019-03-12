package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteDataModel;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link FrontEndRouteVO} to {@link ExtendedFrontEndRouteDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter implements Converter<FrontEndRouteVO, ExtendedFrontEndRouteDataModel> {

    private DateConverter dateConverter;

    @Autowired
    public FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public ExtendedFrontEndRouteDataModel convert(FrontEndRouteVO source) {

        return ExtendedFrontEndRouteDataModel.getExtendedBuilder()
                .withRouteId(source.getRouteId())
                .withName(source.getName())
                .withUrl(source.getUrl())
                .withEnabled(source.isEnabled())
                .withSequenceNumber(source.getSequenceNumber())
                .withId(source.getId())
                .withType(source.getType().name())
                .withAuthRequirement(source.getAuthRequirement().name())
                .withCreated(dateConverter.convert(source.getCreated()))
                .withLastModified(dateConverter.convert(source.getLastModified()))
                .build();
    }
}
