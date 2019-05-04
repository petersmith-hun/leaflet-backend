package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.api.rest.response.sitemap.SitemapLocationItem;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.SitemapBridgeService;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance test for {@code /sitemap.xml} endpoint.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuite
public class SitemapControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final String SITEMAP_LOCATION_NODE_PATTERN = "<url><loc>%s</loc></url>";
    private static final String URLSET_START_TAG = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";
    private static final String URLSET_END_TAG = "</urlset>";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SitemapBridgeService sitemapBridgeService;

    @Autowired
    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @Value("${bridge.clients.leaflet.host-url}/sitemap.xml")
    private String serviceURL;

    @Test
    public void shouldGetSitemapAsXML() {

        // given
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        // when
        ResponseEntity<String> result = testRestTemplate.exchange(serviceURL, HttpMethod.GET, httpEntity, String.class);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getBody().startsWith(URLSET_START_TAG), is(true));
        assertThat(result.getBody().endsWith(URLSET_END_TAG), is(true));
        frontEndRoutingSupportFacade.getSitemap()
                .forEach(route -> assertThat(result.getBody().contains(String.format(SITEMAP_LOCATION_NODE_PATTERN, route.getUrl())), is(true)));
    }

    @Test
    public void shouldGetSitemapAsJSON() throws CommunicationFailureException {

        // when
        Sitemap result = sitemapBridgeService.getSitemap();

        // then
        assertThat(result, notNullValue());
        List<String> frontEndRouteVOList = frontEndRoutingSupportFacade.getSitemap().stream()
                .map(FrontEndRouteVO::getUrl)
                .collect(Collectors.toList());
        List<String> locations = result.getSitemapLocationItemList().stream()
                .map(SitemapLocationItem::getLocation)
                .collect(Collectors.toList());
        assertThat(locations.containsAll(frontEndRouteVOList), is(true));
    }
}
