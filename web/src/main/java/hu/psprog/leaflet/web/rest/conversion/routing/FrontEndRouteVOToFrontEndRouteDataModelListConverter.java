package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteListDataModel;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link FrontEndRouteVO} objects to {@link FrontEndRouteListDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteVOToFrontEndRouteDataModelListConverter implements Converter<List<FrontEndRouteVO>, FrontEndRouteListDataModel> {

    private FrontEndRouteVOToFrontEndRouteDataModelConverter singleConverter;

    @Autowired
    public FrontEndRouteVOToFrontEndRouteDataModelListConverter(FrontEndRouteVOToFrontEndRouteDataModelConverter singleConverter) {
        this.singleConverter = singleConverter;
    }

    @Override
    public FrontEndRouteListDataModel convert(List<FrontEndRouteVO> source) {

        FrontEndRouteListDataModel.FrontEndRouteListDataModelBuilder builder = FrontEndRouteListDataModel.getBuilder();
        source.forEach(frontEndRouteVO -> builder.withItem(singleConverter.convert(frontEndRouteVO)));

        return builder.build();
    }
}
