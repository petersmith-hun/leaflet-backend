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
 * Unit tests for {@link FrontEndRouteVOToFrontEndRouteConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRouteVOToFrontEndRouteConverterTest extends ConversionTestObjects {

    @InjectMocks
    private FrontEndRouteVOToFrontEndRouteConverter converter;

    @Test
    public void shouldConvert() {

        // when
        FrontEndRoute result = converter.convert(FRONT_END_ROUTE_VO);

        // when
        assertThat(result, equalTo(FRONT_END_ROUTE_MINIMUM));
    }

    @Test
    public void shouldConvertEmptyObject() {

        // when
        FrontEndRoute result = converter.convert(FrontEndRouteVO.getBuilder().build());

        // when
        assertThat(result, equalTo(FrontEndRoute.getBuilder().build()));
    }
}