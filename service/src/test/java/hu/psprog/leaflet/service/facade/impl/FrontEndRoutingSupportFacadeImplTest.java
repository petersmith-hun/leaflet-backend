package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FrontEndRoutingSupportFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class FrontEndRoutingSupportFacadeImplTest {

    private static final Long CONTROL_ID = 1L;
    private static final String URL = "url-1";
    private static final String URL_WITH_LEADING_SLASH = "/" + URL;
    private static final String SERVER_NAME = "http://host:1234";
    private static final String SERVER_NAME_WITH_TRAILING_SLASH = SERVER_NAME + "/";
    private static final String EXPECTED_GENERATED_URL = SERVER_NAME + "/" + URL;
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO = FrontEndRouteVO.getBuilder().withUrl(URL).build();
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST = Collections.singletonList(FRONT_END_ROUTE_VO);

    @Mock
    private FrontEndRoutingSupportService frontEndRoutingSupportService;

    private FrontEndRoutingSupportFacadeImpl frontEndRoutingSupportFacade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        frontEndRoutingSupportFacade = new FrontEndRoutingSupportFacadeImpl(frontEndRoutingSupportService, SERVER_NAME);
    }

    @Test
    public void shouldGetStaticRoutes() {

        // given
        given(frontEndRoutingSupportService.getHeaderMenu()).willReturn(FRONT_END_ROUTE_VO_LIST);
        given(frontEndRoutingSupportService.getFooterMenu()).willReturn(FRONT_END_ROUTE_VO_LIST);
        given(frontEndRoutingSupportService.getStandaloneRoutes()).willReturn(FRONT_END_ROUTE_VO_LIST);

        // when
        Map<FrontEndRouteType, List<FrontEndRouteVO>> result = frontEndRoutingSupportFacade.getStaticRoutes();

        // then
        assertThat(result.get(FrontEndRouteType.HEADER_MENU), equalTo(FRONT_END_ROUTE_VO_LIST));
        assertThat(result.get(FrontEndRouteType.FOOTER_MENU), equalTo(FRONT_END_ROUTE_VO_LIST));
        assertThat(result.get(FrontEndRouteType.STANDALONE), equalTo(FRONT_END_ROUTE_VO_LIST));
    }

    @Test
    @Parameters(source = SitemapDataProvider.class)
    public void shouldGetSitemap(String hostName, String url) {

        // given
        FrontEndRouteVO inputRouteMask = FrontEndRouteVO.getBuilder()
                .withUrl(url)
                .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
                .build();
        FrontEndRouteVO inputRouteMaskToBeSkipped = FrontEndRouteVO.getBuilder()
                .withUrl("/url-skipped")
                .withAuthRequirement(FrontEndRouteAuthRequirement.AUTHENTICATED)
                .build();
        List<FrontEndRouteVO> inputRoutes = Arrays.asList(inputRouteMask, inputRouteMaskToBeSkipped);
        given(frontEndRoutingSupportService.getHeaderMenu()).willReturn(inputRoutes);
        given(frontEndRoutingSupportService.getFooterMenu()).willReturn(inputRoutes);
        given(frontEndRoutingSupportService.getStandaloneRoutes()).willReturn(inputRoutes);
        frontEndRoutingSupportFacade = new FrontEndRoutingSupportFacadeImpl(frontEndRoutingSupportService, hostName);

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportFacade.getSitemap();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getUrl(), equalTo(EXPECTED_GENERATED_URL));
    }

    @Test
    public void shouldCreateOne() throws ServiceException {

        // given
        given(frontEndRoutingSupportService.createOne(FRONT_END_ROUTE_VO)).willReturn(CONTROL_ID);
        given(frontEndRoutingSupportService.getOne(CONTROL_ID)).willReturn(FRONT_END_ROUTE_VO);

        // when
        FrontEndRouteVO result = frontEndRoutingSupportFacade.createOne(FRONT_END_ROUTE_VO);

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_VO));
    }

    @Test
    public void shouldGetOne() throws ServiceException {

        // given
        given(frontEndRoutingSupportService.getOne(CONTROL_ID)).willReturn(FRONT_END_ROUTE_VO);

        // when
        FrontEndRouteVO result = frontEndRoutingSupportFacade.getOne(CONTROL_ID);

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_VO));
    }

    @Test
    public void shouldGetAll() {

        // given
        given(frontEndRoutingSupportService.getAll()).willReturn(FRONT_END_ROUTE_VO_LIST);

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportFacade.getAll();

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_VO_LIST));
    }

    @Test
    public void shouldCount() {

        // given
        given(frontEndRoutingSupportService.count()).willReturn(3L);

        // when
        Long result = frontEndRoutingSupportFacade.count();

        // then
        assertThat(result, equalTo(3L));
    }

    @Test
    public void shouldUpdate() throws ServiceException {

        // given
        given(frontEndRoutingSupportService.getOne(CONTROL_ID)).willReturn(FRONT_END_ROUTE_VO);

        // when
        FrontEndRouteVO result = frontEndRoutingSupportFacade.updateOne(CONTROL_ID, FRONT_END_ROUTE_VO);

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_VO));
        verify(frontEndRoutingSupportService).updateOne(CONTROL_ID, FRONT_END_ROUTE_VO);
    }

    @Test
    public void shouldDeletePermanently() throws ServiceException {

        // when
        frontEndRoutingSupportFacade.deletePermanently(CONTROL_ID);

        // then
        verify(frontEndRoutingSupportService).deleteByID(CONTROL_ID);
    }

    @Test
    public void shouldChangeStatusIfEnabled() throws ServiceException {

        // given
        given(frontEndRoutingSupportService.getOne(CONTROL_ID)).willReturn(FrontEndRouteVO.getBuilder().withEnabled(true).build());

        // when
        frontEndRoutingSupportFacade.changeStatus(CONTROL_ID);

        // then
        verify(frontEndRoutingSupportService).disable(CONTROL_ID);
    }

    @Test
    public void shouldChangeStatusIfDisabled() throws ServiceException {

        // given
        given(frontEndRoutingSupportService.getOne(CONTROL_ID)).willReturn(FrontEndRouteVO.getBuilder().withEnabled(false).build());

        // when
        frontEndRoutingSupportFacade.changeStatus(CONTROL_ID);

        // then
        verify(frontEndRoutingSupportService).enable(CONTROL_ID);
    }

    public static class SitemapDataProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {SERVER_NAME, URL},
                    new Object[] {SERVER_NAME_WITH_TRAILING_SLASH, URL},
                    new Object[] {SERVER_NAME, URL_WITH_LEADING_SLASH},
                    new Object[] {SERVER_NAME_WITH_TRAILING_SLASH, URL_WITH_LEADING_SLASH},
            };
        }
    }
}