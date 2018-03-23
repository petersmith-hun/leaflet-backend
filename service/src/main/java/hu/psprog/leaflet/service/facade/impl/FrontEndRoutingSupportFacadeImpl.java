package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FrontEndRoutingSupportFacade}.
 *
 * @author Peter Smith
 */
@Service
public class FrontEndRoutingSupportFacadeImpl implements FrontEndRoutingSupportFacade {

    private static final String URL_MASK = "%s://%s%s";
    private static final String RELATIVE_URL_PREFIX = "/";

    private FrontEndRoutingSupportService frontEndRoutingSupportService;

    @Autowired
    public FrontEndRoutingSupportFacadeImpl(FrontEndRoutingSupportService frontEndRoutingSupportService) {
        this.frontEndRoutingSupportService = frontEndRoutingSupportService;
    }

    @Override
    public Map<FrontEndRouteType, List<FrontEndRouteVO>> getStaticRoutes() {

        Map<FrontEndRouteType, List<FrontEndRouteVO>> staticRouteMap = new HashMap<>();
        staticRouteMap.put(FrontEndRouteType.HEADER_MENU, frontEndRoutingSupportService.getHeaderMenu());
        staticRouteMap.put(FrontEndRouteType.FOOTER_MENU, frontEndRoutingSupportService.getFooterMenu());
        staticRouteMap.put(FrontEndRouteType.STANDALONE, frontEndRoutingSupportService.getStandaloneRoutes());

        return staticRouteMap;
    }

    @Override
    public List<FrontEndRouteVO> getSitemap(String protocol, String host) {

        List<FrontEndRouteVO> sitemap = frontEndRoutingSupportService.getDynamicRoutes();
        sitemap.addAll(frontEndRoutingSupportService.getHeaderMenu());
        sitemap.addAll(frontEndRoutingSupportService.getFooterMenu());
        sitemap.addAll(frontEndRoutingSupportService.getStandaloneRoutes());

        return sitemap.stream()
                .distinct()
                .map(route -> FrontEndRouteVO.getBuilder()
                        .withUrl(buildAbsoluteURL(protocol, host, route.getUrl()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public FrontEndRouteVO createOne(FrontEndRouteVO entity) throws ServiceException {
        Long createdID = frontEndRoutingSupportService.createOne(entity);
        return frontEndRoutingSupportService.getOne(createdID);
    }

    @Override
    public FrontEndRouteVO getOne(Long id) throws ServiceException {
        return frontEndRoutingSupportService.getOne(id);
    }

    @Override
    public List<FrontEndRouteVO> getAll() {
        return frontEndRoutingSupportService.getAll();
    }

    @Override
    public Long count() {
        return frontEndRoutingSupportService.count();
    }

    @Override
    public FrontEndRouteVO updateOne(Long id, FrontEndRouteVO updatedFrontEndRoute) throws ServiceException {
        frontEndRoutingSupportService.updateOne(id, updatedFrontEndRoute);
        return frontEndRoutingSupportService.getOne(id);
    }

    @Override
    public void deletePermanently(Long id) throws ServiceException {
        frontEndRoutingSupportService.deleteByID(id);
    }

    @Override
    public FrontEndRouteVO changeStatus(Long id) throws ServiceException {

        FrontEndRouteVO current = frontEndRoutingSupportService.getOne(id);
        if (current.isEnabled()) {
            frontEndRoutingSupportService.disable(id);
        } else {
            frontEndRoutingSupportService.enable(id);
        }

        return frontEndRoutingSupportService.getOne(id);
    }

    private String buildAbsoluteURL(String protocol, String host, String relativeURL) {
        return String.format(URL_MASK, protocol, host, relativeURL.startsWith(RELATIVE_URL_PREFIX)
                ? relativeURL
                : RELATIVE_URL_PREFIX + relativeURL);
    }
}
