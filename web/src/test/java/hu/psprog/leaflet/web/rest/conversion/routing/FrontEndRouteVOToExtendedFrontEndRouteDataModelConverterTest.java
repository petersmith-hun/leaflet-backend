package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRouteVOToExtendedFrontEndRouteDataModelConverterTest extends ConversionTestObjects {

    @InjectMocks
    private FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // when
        ExtendedFrontEndRouteDataModel result = converter.convert(FRONT_END_ROUTE_VO);

        // then
        assertThat(result, equalTo(EXTENDED_FRONT_END_ROUTE_DATA_MODEL));
    }
}