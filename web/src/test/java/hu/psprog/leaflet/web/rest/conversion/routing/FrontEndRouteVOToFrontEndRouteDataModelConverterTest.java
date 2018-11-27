package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteVOToFrontEndRouteDataModelConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRouteVOToFrontEndRouteDataModelConverterTest extends ConversionTestObjects {

    @InjectMocks
    private FrontEndRouteVOToFrontEndRouteDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // when
        FrontEndRouteDataModel result = converter.convert(FRONT_END_ROUTE_VO);

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_DATA_MODEL));
    }
}