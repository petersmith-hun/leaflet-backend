package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteToFrontEndRouteVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRouteToFrontEndRouteVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private FrontEndRouteToFrontEndRouteVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        FrontEndRouteVO result = converter.convert(FRONT_END_ROUTE);

        // when
        assertThat(result, equalTo(FRONT_END_ROUTE_VO));
    }

    @Test
    public void shouldConvertEmptyObject() {

        // when
        FrontEndRouteVO result = converter.convert(FrontEndRoute.getBuilder().build());

        // when
        assertThat(result, equalTo(FrontEndRouteVO.getBuilder().build()));
    }
}