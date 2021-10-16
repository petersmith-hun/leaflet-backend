package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
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