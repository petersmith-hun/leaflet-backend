package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteListDataModel;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link FrontEndRouteVO} objects to {@link ExtendedFrontEndRouteListDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteVOToExtendedFrontEndRouteDataModelListConverter implements Converter<List<FrontEndRouteVO>, ExtendedFrontEndRouteListDataModel> {

    private final FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter singleConverter;

    @Autowired
    public FrontEndRouteVOToExtendedFrontEndRouteDataModelListConverter(FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter singleConverter) {
        this.singleConverter = singleConverter;
    }

    @Override
    public ExtendedFrontEndRouteListDataModel convert(List<FrontEndRouteVO> source) {

        return ExtendedFrontEndRouteListDataModel.getBuilder()
                .withRoutes(source.stream()
                        .map(singleConverter::convert)
                        .toList())
                .build();
    }
}
