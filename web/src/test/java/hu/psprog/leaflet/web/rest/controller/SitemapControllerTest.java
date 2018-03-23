package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.rest.model.Sitemap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link SitemapController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class SitemapControllerTest extends AbstractControllerBaseTest {

    private static final String URL_1 = "url-1";
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST = Collections.singletonList(FrontEndRouteVO.getBuilder().withUrl(URL_1).build());
    private static final Sitemap SITEMAP = Sitemap.getBuilder().withLocation(URL_1).build();
    private static final String PROTOCOL = "http";
    private static final String HOST = "localhost";

    @Mock
    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @InjectMocks
    private SitemapController sitemapController;

    @Test
    public void shouldGetSitemap() {

        // given
        given(httpServletRequest.getScheme()).willReturn(PROTOCOL);
        given(httpServletRequest.getServerName()).willReturn(HOST);
        given(frontEndRoutingSupportFacade.getSitemap(PROTOCOL, HOST)).willReturn(FRONT_END_ROUTE_VO_LIST);
        given(conversionService.convert(FRONT_END_ROUTE_VO_LIST, Sitemap.class)).willReturn(SITEMAP);

        // then
        ResponseEntity<Sitemap> result = sitemapController.getSitemap(httpServletRequest);

        // then
        assertResponse(result, HttpStatus.OK, SITEMAP);
    }
}