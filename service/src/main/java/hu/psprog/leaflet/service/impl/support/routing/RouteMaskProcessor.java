package hu.psprog.leaflet.service.impl.support.routing;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;

import java.util.List;

/**
 * Front-end routing mask processor.
 * Able to generate real routes based on given route mask.
 *
 * @author Peter Smith
 */
public interface RouteMaskProcessor {

    /**
     * Decides if the implementing processor is able to process given mask.
     * Decision is made based on the {@link FrontEndRouteType} parameter in {@link FrontEndRouteVO} object.
     *
     * @param frontEndRouteVO {@link FrontEndRouteVO} containing route information.
     * @return {@code true} if given route type is supported, {@code false} otherwise
     */
    boolean supports(FrontEndRouteVO frontEndRouteVO);

    /**
     * Generates real routes based on route mask from {@link FrontEndRouteVO} object.
     *
     * @param frontEndRouteVO {@link FrontEndRouteVO} containing the route mask
     * @return list of generated routes as {@link FrontEndRouteVO}
     */
    List<FrontEndRouteVO> process(FrontEndRouteVO frontEndRouteVO);
}
