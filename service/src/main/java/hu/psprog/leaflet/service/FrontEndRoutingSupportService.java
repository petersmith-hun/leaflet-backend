package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;

import java.util.List;

/**
 * Routing translation support operations for visitor-side front-end application.
 * This service's purpose is to provide ordered menus and route masks for front-end.
 * Currently it is able to differentiate header and footer menus, and route masks.
 * Header and footer menus are an ordered list of menu items with explicitly mapped URLs.
 * Route masks are for dynamically generated URLs (basically entry URLs).
 *
 * @author Peter Smith
 */
public interface FrontEndRoutingSupportService extends CreateOperationCapableService<FrontEndRouteVO, Long>,
        ReadOperationCapableService<FrontEndRouteVO, Long>,
        UpdateOperationCapableService<FrontEndRouteVO, FrontEndRouteVO, Long>,
        DeleteOperationCapableService<FrontEndRouteVO, Long>,
        StatusChangeCapableService<Long> {

    /**
     * Returns sorted list of routes marked as header menu items.
     *
     * @return List of {@link FrontEndRouteVO} objects or empty list if no items can be found
     */
    List<FrontEndRouteVO> getHeaderMenu();

    /**
     * Returns sorted list of routes marked as footer menu items.
     *
     * @return List of {@link FrontEndRouteVO} objects or empty list if no items can be found
     */
    List<FrontEndRouteVO> getFooterMenu();

    /**
     * Returns sorted list of routes marked as standalone routes.
     *
     * @return List of {@link FrontEndRouteVO} objects or empty list if no items can be found
     */
    List<FrontEndRouteVO> getStandaloneRoutes();

    /**
     * Generates and returns list of routes built from route masks.
     *
     * @return List of {@link FrontEndRouteVO} objects or empty list if no items can be found
     */
    List<FrontEndRouteVO> getDynamicRoutes();
}
