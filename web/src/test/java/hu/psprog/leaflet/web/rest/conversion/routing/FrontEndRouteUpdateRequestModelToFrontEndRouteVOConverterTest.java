package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteUpdateRequestModelToFrontEndRouteVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRouteUpdateRequestModelToFrontEndRouteVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private FrontEndRouteUpdateRequestModelToFrontEndRouteVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        FrontEndRouteVO result = converter.convert(FRONT_END_ROUTE_UPDATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_VO_CREATE));
    }
}