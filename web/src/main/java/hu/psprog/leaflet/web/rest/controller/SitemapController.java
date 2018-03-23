package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.rest.model.Sitemap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Google-compatible sitemap.xml provider controller.
 *
 * @author Peter Smith
 */
@RestController
public class SitemapController extends BaseController {

    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @Autowired
    public SitemapController(FrontEndRoutingSupportFacade frontEndRoutingSupportFacade) {
        this.frontEndRoutingSupportFacade = frontEndRoutingSupportFacade;
    }

    /**
     * GET /sitemap.xml
     * Returns sitemap.
     *
     * @param request {@link HttpServletRequest} object to extract protocol and host's domain
     * @return generated sitemap
     */
    @RequestMapping(path = BaseController.BASE_PATH_SITEMAP, method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Sitemap> getSitemap(HttpServletRequest request) {

        List<FrontEndRouteVO> routes = frontEndRoutingSupportFacade.getSitemap(request.getScheme(), request.getServerName());

        return ResponseEntity
                .ok(conversionService.convert(routes, Sitemap.class));
    }
}
