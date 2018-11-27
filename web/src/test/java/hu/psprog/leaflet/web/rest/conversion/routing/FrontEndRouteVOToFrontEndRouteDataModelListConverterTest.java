package hu.psprog.leaflet.web.rest.conversion.routing;

import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link FrontEndRouteVOToFrontEndRouteDataModelListConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRouteVOToFrontEndRouteDataModelListConverterTest extends ConversionTestObjects {

    @Mock
    private FrontEndRouteVOToFrontEndRouteDataModelConverter singleConverter;

    @InjectMocks
    private FrontEndRouteVOToFrontEndRouteDataModelListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(singleConverter.convert(FRONT_END_ROUTE_VO)).willReturn(FRONT_END_ROUTE_DATA_MODEL);

        // when
        FrontEndRouteListDataModel result = converter.convert(Collections.singletonList(FRONT_END_ROUTE_VO));

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_LIST_DATA_MODEL));
    }
}