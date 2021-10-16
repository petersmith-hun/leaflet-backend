package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link FrontEndRouteVOToExtendedFrontEndRouteDataModelListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class FrontEndRouteVOToExtendedFrontEndRouteDataModelListConverterTest extends ConversionTestObjects {

    @Mock
    private FrontEndRouteVOToExtendedFrontEndRouteDataModelConverter singleConverter;

    @InjectMocks
    private FrontEndRouteVOToExtendedFrontEndRouteDataModelListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(singleConverter.convert(FRONT_END_ROUTE_VO)).willReturn(EXTENDED_FRONT_END_ROUTE_DATA_MODEL);

        // when
        ExtendedFrontEndRouteListDataModel result = converter.convert(Collections.singletonList(FRONT_END_ROUTE_VO));

        // then
        assertThat(result, equalTo(EXTENDED_FRONT_END_ROUTE_LIST_DATA_MODEL));
    }
}