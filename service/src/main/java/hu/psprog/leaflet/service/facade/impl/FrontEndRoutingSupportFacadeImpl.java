package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndRoutingSupportFacadeImpl.class);

    private static final String RELATIVE_URL_PREFIX = "/";

    private FrontEndRoutingSupportService frontEndRoutingSupportService;
    private String serverName;

    @Autowired
    public FrontEndRoutingSupportFacadeImpl(FrontEndRoutingSupportService frontEndRoutingSupportService,
                                            @Value("${sitemap.server-name}") String serverName) {
        this.frontEndRoutingSupportService = frontEndRoutingSupportService;
        this.serverName = stripTrailingSlash(serverName);
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
    public List<FrontEndRouteVO> getSitemap() {

        LOGGER.info("Sitemap has been requested");

        List<FrontEndRouteVO> sitemap = frontEndRoutingSupportService.getDynamicRoutes();
        sitemap.addAll(frontEndRoutingSupportService.getHeaderMenu());
        sitemap.addAll(frontEndRoutingSupportService.getFooterMenu());
        sitemap.addAll(frontEndRoutingSupportService.getStandaloneRoutes());

        return sitemap.stream()
                .filter(frontEndRouteVO -> frontEndRouteVO.getAuthRequirement() == FrontEndRouteAuthRequirement.SHOW_ALWAYS)
                .map(route -> FrontEndRouteVO.getBuilder()
                        .withUrl(buildAbsoluteURL(route.getUrl()))
                        .build())
                .distinct()
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

    private String stripTrailingSlash(String serverName) {
        return serverName.endsWith(RELATIVE_URL_PREFIX)
                ? serverName.substring(0, serverName.length() - 1)
                : serverName;
    }

    private String buildAbsoluteURL(String relativeURL) {
        return serverName + normalizeRelativeURL(relativeURL);
    }

    private String normalizeRelativeURL(String relativeURL) {
        return relativeURL.startsWith(RELATIVE_URL_PREFIX)
                ? relativeURL
                : RELATIVE_URL_PREFIX + relativeURL;
    }
}
