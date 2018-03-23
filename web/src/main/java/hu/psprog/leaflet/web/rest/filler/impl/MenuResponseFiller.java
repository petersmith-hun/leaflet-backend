package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.api.rest.response.common.MenuDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteDataModel;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Extends JSON response with front-end menu data.
 *
 * @author Peter Smith
 */
@Component
public class MenuResponseFiller extends AbstractAjaxRequestAwareResponseFiller {

    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;
    private ConversionService conversionService;

    @Autowired
    public MenuResponseFiller(HttpServletRequest httpServletRequest, FrontEndRoutingSupportFacade frontEndRoutingSupportFacade, ConversionService conversionService) {
        super(httpServletRequest);
        this.frontEndRoutingSupportFacade = frontEndRoutingSupportFacade;
        this.conversionService = conversionService;
    }

    @Override
    public void fill(WrapperBodyDataModel.WrapperBodyDataModelBuilder wrapperBodyDataModelBuilder) {

        Map<FrontEndRouteType, List<FrontEndRouteVO>> allRoutesByType = frontEndRoutingSupportFacade.getStaticRoutes();
        MenuDataModel menuDataModel = MenuDataModel.getBuilder()
                .withHeader(extractRoutes(FrontEndRouteType.HEADER_MENU, allRoutesByType))
                .withFooter(extractRoutes(FrontEndRouteType.FOOTER_MENU, allRoutesByType))
                .withStandalone(extractRoutes(FrontEndRouteType.STANDALONE, allRoutesByType))
                .build();

        wrapperBodyDataModelBuilder.withMenu(menuDataModel);
    }

    private List<FrontEndRouteDataModel> extractRoutes(FrontEndRouteType type, Map<FrontEndRouteType, List<FrontEndRouteVO>> allRoutes) {

        return Optional.ofNullable(allRoutes.get(type))
                .map(routesInType -> routesInType.stream()
                        .filter(Objects::nonNull)
                        .map(route -> conversionService.convert(route, FrontEndRouteDataModel.class))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
