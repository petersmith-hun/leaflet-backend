package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * @return generated sitemap
     */
    @RequestMapping(path = BaseController.BASE_PATH_SITEMAP, method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Sitemap> getSitemap() {

        List<FrontEndRouteVO> routes = frontEndRoutingSupportFacade.getSitemap();

        return ResponseEntity
                .ok(conversionService.convert(routes, Sitemap.class));
    }
}
