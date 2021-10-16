package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteVOToFrontEndRouteDataModelConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
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