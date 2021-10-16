package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteDataModel;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link MenuResponseFiller}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class MenuResponseFillerTest {

    private static final String URL_1 = "url-1";
    private static final String URL_2 = "url-2";
    private static final String URL_3 = "url-3";
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_1 = prepareFrontEndRouteVO(URL_1);
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_2 = prepareFrontEndRouteVO(URL_2);
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_3 = prepareFrontEndRouteVO(URL_3);
    private static final Map<FrontEndRouteType, List<FrontEndRouteVO>> STATIC_ROUTES = prepareStaticRoutes();

    @Mock
    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @Mock(lenient = true)
    private ConversionService conversionService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private MenuResponseFiller menuResponseFiller;

    @Test
    public void shouldFill() {

        // given
        WrapperBodyDataModel.WrapperBodyDataModelBuilder builder = WrapperBodyDataModel.getBuilder();
        given(frontEndRoutingSupportFacade.getStaticRoutes()).willReturn(STATIC_ROUTES);
        given(conversionService.convert(FRONT_END_ROUTE_VO_1, FrontEndRouteDataModel.class)).willReturn(FrontEndRouteDataModel.getBuilder().withUrl(URL_1).build());
        given(conversionService.convert(FRONT_END_ROUTE_VO_2, FrontEndRouteDataModel.class)).willReturn(FrontEndRouteDataModel.getBuilder().withUrl(URL_2).build());
        given(conversionService.convert(FRONT_END_ROUTE_VO_3, FrontEndRouteDataModel.class)).willReturn(FrontEndRouteDataModel.getBuilder().withUrl(URL_3).build());

        // when
        menuResponseFiller.fill(builder);

        // then
        WrapperBodyDataModel builtWrapper = builder.build();
        assertThat(builtWrapper.getMenu(), notNullValue());
        assertThat(builtWrapper.getMenu().getHeader().size(), equalTo(2));
        assertThat(builtWrapper.getMenu().getHeader().get(0).getUrl(), equalTo(URL_1));
        assertThat(builtWrapper.getMenu().getHeader().get(1).getUrl(), equalTo(URL_2));
        assertThat(builtWrapper.getMenu().getFooter().size(), equalTo(0));
        assertThat(builtWrapper.getMenu().getStandalone().size(), equalTo(1));
        assertThat(builtWrapper.getMenu().getStandalone().get(0).getUrl(), equalTo(URL_3));
    }

    @Test
    public void shouldFillWithNullSections() {

        // given
        WrapperBodyDataModel.WrapperBodyDataModelBuilder builder = WrapperBodyDataModel.getBuilder();

        // when
        menuResponseFiller.fill(builder);

        // then
        WrapperBodyDataModel builtWrapper = builder.build();
        assertThat(builtWrapper.getMenu(), notNullValue());
        assertThat(builtWrapper.getMenu().getHeader().isEmpty(), is(true));
        assertThat(builtWrapper.getMenu().getFooter().isEmpty(), is(true));
        assertThat(builtWrapper.getMenu().getStandalone().isEmpty(), is(true));
    }

    @Test
    public void shouldFillNonAjaxRequest() {

        // given
        given(httpServletRequest.getAttribute(RequestParameter.IS_AJAX_REQUEST)).willReturn(false);

        // when
        boolean result = menuResponseFiller.shouldFill();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldFillResponseIfAjaxParameterIsMissing() {

        // when
        boolean result = menuResponseFiller.shouldFill();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldNotFillAjaxRequest() {

        // given
        given(httpServletRequest.getAttribute(RequestParameter.IS_AJAX_REQUEST)).willReturn(true);

        // when
        boolean result = menuResponseFiller.shouldFill();

        // then
        assertThat(result, is(false));
    }

    private static Map<FrontEndRouteType, List<FrontEndRouteVO>> prepareStaticRoutes() {

        Map<FrontEndRouteType, List<FrontEndRouteVO>> routeMap = new HashMap<>();
        routeMap.put(FrontEndRouteType.HEADER_MENU, Arrays.asList(FRONT_END_ROUTE_VO_1, FRONT_END_ROUTE_VO_2));
        routeMap.put(FrontEndRouteType.FOOTER_MENU, Collections.emptyList());
        routeMap.put(FrontEndRouteType.STANDALONE, Arrays.asList(FRONT_END_ROUTE_VO_3, null));

        return routeMap;
    }

    private static FrontEndRouteVO prepareFrontEndRouteVO(String url) {

        return FrontEndRouteVO.getBuilder()
                .withUrl(url)
                .build();
    }
}