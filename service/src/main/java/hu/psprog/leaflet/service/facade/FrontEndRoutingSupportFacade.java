package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;

import java.util.List;
import java.util.Map;

/**
 * Facade for {@link FrontEndRoutingSupportService}.
 *
 * @author Peter Smith
 */
public interface FrontEndRoutingSupportFacade {

    /**
     * Returns map of static routes.
     * Map key is the route type and the value is a list of routes.
     *
     * @return map of static routes
     */
    Map<FrontEndRouteType, List<FrontEndRouteVO>> getStaticRoutes();

    /**
     * Generates and returns sitemap.
     * Sitemap contains all static and generated dynamic routes.
     *
     * @return List of {@link FrontEndRouteVO} objects or empty list if no items can be found
     */
    List<FrontEndRouteVO> getSitemap();

    /**
     * Passes front-end route item for persistence layer and returns the newly created one.
     *
     * @param entity {@link FrontEndRouteVO} value object
     * @return created front-end route item data
     */
    FrontEndRouteVO createOne(FrontEndRouteVO entity) throws ServiceException;

    /**
     * Retrieves entity of type {@link FrontEndRouteVO} specified by ID.
     *
     * @param id ID of the front-end route item
     * @return front-end route item identified by given identifier
     */
    FrontEndRouteVO getOne(Long id) throws ServiceException;

    /**
     * Retrieves all entity of type {@link FrontEndRouteVO}.
     *
     * @return list of all entities of type {@link FrontEndRouteVO}
     */
    List<FrontEndRouteVO> getAll();

    /**
     * Updates front-end route item specified by given ID. Returns updated front-end route item.
     *
     * @param id ID of front-end route item
     * @param updatedFrontEndRoute updated {@link FrontEndRouteVO}
     * @return updated front-end route item
     */
    FrontEndRouteVO updateOne(Long id, FrontEndRouteVO updatedFrontEndRoute) throws ServiceException;

    /**
     * Deletes front-end route item by its identifier.
     *
     * @param id ID of the front-end route item to delete
     */
    void deletePermanently(Long id) throws ServiceException;

    /**
     * Changes front-end route item status.
     * If front-end route item is currently enabled, status will be updated to disabled and backwards.
     *
     * @param id ID of front-end route item to change status of
     * @return updated front-end route item data
     * @throws ServiceException if front-end route item cannot be found or status change cannot be performed
     */
    FrontEndRouteVO changeStatus(Long id) throws ServiceException;
}
