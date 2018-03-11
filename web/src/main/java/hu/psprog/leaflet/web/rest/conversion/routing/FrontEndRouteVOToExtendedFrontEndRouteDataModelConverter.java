package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteDataModel;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link FrontEndRouteVO} to {@link ExtendedFrontEndRouteDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter implements Converter<FrontEndRouteVO, ExtendedFrontEndRouteDataModel> {

    private CommonFormatter commonFormatter;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter(CommonFormatter commonFormatter, HttpServletRequest httpServletRequest) {
        this.commonFormatter = commonFormatter;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ExtendedFrontEndRouteDataModel convert(FrontEndRouteVO source) {

        return ExtendedFrontEndRouteDataModel.getExtendedBuilder()
                .withName(source.getName())
                .withUrl(source.getUrl())
                .withEnabled(source.isEnabled())
                .withSequenceNumber(source.getSequenceNumber())
                .withId(source.getId())
                .withType(source.getType().name())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .build();
    }
}
