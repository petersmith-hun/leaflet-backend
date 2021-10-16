package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
public class SitemapControllerTest extends AbstractControllerBaseTest {

    private static final String URL_1 = "url-1";
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST = Collections.singletonList(FrontEndRouteVO.getBuilder().withUrl(URL_1).build());
    private static final Sitemap SITEMAP = Sitemap.getBuilder().withLocation(URL_1).build();

    @Mock
    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @InjectMocks
    private SitemapController sitemapController;

    @Test
    public void shouldGetSitemap() {

        // given
        given(frontEndRoutingSupportFacade.getSitemap()).willReturn(FRONT_END_ROUTE_VO_LIST);
        given(conversionService.convert(FRONT_END_ROUTE_VO_LIST, Sitemap.class)).willReturn(SITEMAP);

        // then
        ResponseEntity<Sitemap> result = sitemapController.getSitemap();

        // then
        assertResponse(result, HttpStatus.OK, SITEMAP);
    }
}