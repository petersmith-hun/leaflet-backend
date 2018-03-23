package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

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

    private static final String PROTOCOL = "http";
    private static final String HOST = "localhost";
    private static final String SITEMAP_LOCATION_NODE_PATTERN = "<url><loc>%s</loc></url>";
    private static final String URLSET_START_TAG = "<urlset>";
    private static final String URLSET_END_TAG = "</urlset>";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @Value("${bridge.baseUrl}/sitemap.xml")
    private String serviceURL;

    @Test
    public void shouldGetSitemap() {

        // when
        ResponseEntity<String> result = testRestTemplate.getForEntity(serviceURL, String.class);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getBody().startsWith(URLSET_START_TAG), is(true));
        assertThat(result.getBody().endsWith(URLSET_END_TAG), is(true));
        frontEndRoutingSupportFacade.getSitemap(PROTOCOL, HOST)
                .forEach(route -> assertThat(result.getBody().contains(String.format(SITEMAP_LOCATION_NODE_PATTERN, route.getUrl())), is(true)));
    }
}
