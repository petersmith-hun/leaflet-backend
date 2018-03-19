package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.fail;
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
    private static final String FRONT_END_ROUTE_NEW = "front_end_route_new";
    private static final String FRONT_END_ROUTE_1 = "front_end_route_1";
    private static final long CONTROL_ID = 1L;
    private static final long NUMBER_OF_ALL_RECORDS = 13L;

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

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldGetOne() throws IOException, ServiceException {

        // given
        FrontEndRouteVO expectedFrontEndRouteVO = testObjectReader.read(FRONT_END_ROUTE_1, TestObjectReader.ObjectDirectory.VO, FrontEndRouteVO.class);

        // when
        FrontEndRouteVO result = frontEndRoutingSupportService.getOne(CONTROL_ID);

        // then
        assertThat(result, equalTo(expectedFrontEndRouteVO));
    }

    @Test
    @Transactional
    public void shouldCreateOne() throws IOException, ServiceException {

        // given
        FrontEndRouteVO frontEndRouteVOToSave = testObjectReader.read(FRONT_END_ROUTE_NEW, TestObjectReader.ObjectDirectory.VO, FrontEndRouteVO.class);

        // when
        Long result = frontEndRoutingSupportService.createOne(frontEndRouteVOToSave);

        // then
        assertThat(frontEndRoutingSupportService.count(), equalTo(1L));
        assertThat(frontEndRoutingSupportService.getOne(result).getName(), equalTo(frontEndRouteVOToSave.getName()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldDeleteById() throws ServiceException {

        // given
        // make an assertion for the initial record count
        assertThat(frontEndRoutingSupportService.count(), equalTo(NUMBER_OF_ALL_RECORDS));

        // when
        frontEndRoutingSupportService.deleteByID(CONTROL_ID);

        // then
        assertThat(frontEndRoutingSupportService.count(), equalTo(12L));
        try {
            frontEndRoutingSupportService.getOne(CONTROL_ID);
            fail("Call should have failed");
        } catch (EntityNotFoundException exc) {
            // we expect this exception
        }
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldCount() {

        // when
        Long result = frontEndRoutingSupportService.count();

        // then
        assertThat(result, equalTo(NUMBER_OF_ALL_RECORDS));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldGetAll() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getAll();

        // then
        assertThat(result.size(), equalTo((int) NUMBER_OF_ALL_RECORDS));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldEnable() throws ServiceException {

        // when
        frontEndRoutingSupportService.enable(3L);

        // then
        assertThat(frontEndRoutingSupportService.getOne(3L).isEnabled(), is(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldDisable() throws ServiceException {

        // when
        frontEndRoutingSupportService.disable(CONTROL_ID);

        // then
        assertThat(frontEndRoutingSupportService.getOne(CONTROL_ID).isEnabled(), is(false));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES)
    public void shouldUpdateOne() throws ServiceException {

        // given
        FrontEndRouteVO updatedFrontEndRouteVO = FrontEndRouteVO.getBuilder()
                .withName("updated name")
                .withUrl("/updated/url/1")
                .withType(FrontEndRouteType.STANDALONE)
                .withRouteId("updated-route-id")
                .withSequenceNumber(8)
                .build();

        // when
        FrontEndRouteVO result = frontEndRoutingSupportService.updateOne(CONTROL_ID, updatedFrontEndRouteVO);

        // then
        assertThat(result, equalTo(frontEndRoutingSupportService.getOne(CONTROL_ID)));
        assertThat(result.getName(), equalTo(updatedFrontEndRouteVO.getName()));
        assertThat(result.getType(), equalTo(updatedFrontEndRouteVO.getType()));
        assertThat(result.getUrl(), equalTo(updatedFrontEndRouteVO.getUrl()));
        assertThat(result.getRouteId(), equalTo(updatedFrontEndRouteVO.getRouteId()));
        assertThat(result.getSequenceNumber(), equalTo(updatedFrontEndRouteVO.getSequenceNumber()));
        assertThat(result.isEnabled(), equalTo(updatedFrontEndRouteVO.isEnabled()));
    }

    private static FrontEndRouteVO prepareDynamicRoute(String name, String url) {
        return FrontEndRouteVO.getBuilder()
                .withName(name)
                .withUrl(url)
                .build();
    }
}
