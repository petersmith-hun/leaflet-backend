package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link FrontEndRoutingSupportServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class FrontEndRoutingSupportServiceImplIT {

    private static final String EXPECTED_DYNAMIC_ROUTE_NAME = "Lorem ipsum dolor sit amet";
    private static final String EXPECTED_DYNAMIC_URL_PATTERN_1 = "/route/dynamic/entry-pattern-1/lorem-ipsum-dolor-sit-amet-20160818";
    private static final String EXPECTED_DYNAMIC_URL_PATTERN_2 = "/route/dynamic/entry-pattern-2/lorem-ipsum-dolor-sit-amet-20160818";
    private static final List<FrontEndRouteVO> EXPECTED_DYNAMIC_ROUTES = Arrays.asList(
            prepareDynamicRoute(EXPECTED_DYNAMIC_ROUTE_NAME, EXPECTED_DYNAMIC_URL_PATTERN_1),
            prepareDynamicRoute(EXPECTED_DYNAMIC_ROUTE_NAME, EXPECTED_DYNAMIC_URL_PATTERN_2));

    @Autowired
    private TestObjectReader testObjectReader;

    @Autowired
    private FrontEndRoutingSupportService frontEndRoutingSupportService;

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldGetHeaderMenu() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getHeaderMenu();

        // then
        assertThat(result.size(), equalTo(2));
        assertThat(result.stream().allMatch(frontEndRouteVO -> frontEndRouteVO.getType() == FrontEndRouteType.HEADER_MENU), is(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldGetFooterMenu() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getFooterMenu();

        // then
        assertThat(result.size(), equalTo(2));
        assertThat(result.stream().allMatch(frontEndRouteVO -> frontEndRouteVO.getType() == FrontEndRouteType.FOOTER_MENU), is(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldGetStandaloneRoutes() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getStandaloneRoutes();

        // then
        assertThat(result.size(), equalTo(2));
        assertThat(result.stream().allMatch(frontEndRouteVO -> frontEndRouteVO.getType() == FrontEndRouteType.STANDALONE), is(true));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES})
    public void shouldGetDynamicRoutes() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getDynamicRoutes();

        // then
        assertThat(result.size(), equalTo(2));
        assertThat(result.containsAll(EXPECTED_DYNAMIC_ROUTES), is(true));
    }

    private static FrontEndRouteVO prepareDynamicRoute(String name, String url) {
        return FrontEndRouteVO.getBuilder()
                .withName(name)
                .withUrl(url)
                .build();
    }
}
