package hu.psprog.leaflet.web.rest.conversion.sitemap;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests {@link FrontEndRouteVOListToSitemapConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRouteVOListToSitemapConverterTest extends ConversionTestObjects {

    private static final String URL_1 = "url-1";
    private static final String URL_2 = "url-2";
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_1 = prepareFrontEndRouteVO(URL_1);
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_2 = prepareFrontEndRouteVO(URL_2);
    private static final Sitemap EXPECTED_SITEMAP = Sitemap.getBuilder()
            .withLocation(URL_1)
            .withLocation(URL_2)
            .build();

    @InjectMocks
    private FrontEndRouteVOListToSitemapConverter converter;

    @Test
    public void shouldConvert() {

        // given
        List<FrontEndRouteVO> routes = Arrays.asList(FRONT_END_ROUTE_VO_1, FRONT_END_ROUTE_VO_2);

        // when
        Sitemap result = converter.convert(routes);

        // then
        assertThat(result, equalTo(EXPECTED_SITEMAP));
    }

    private static FrontEndRouteVO prepareFrontEndRouteVO(String url) {
        return FrontEndRouteVO.getBuilder()
                .withUrl(url)
                .build();
    }
}