package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.routing.FrontEndRouteUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteDataModel;
import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.FrontEndRoutingSupportBridgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Acceptance tests for {@code /routes} endpoints.
 *
 * @author Peter Smith
 */
@LeafletAcceptanceSuite
public class FrontEndRoutingSupportControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_ROUTES = 13;
    private static final String CONTROL_ROUTE = "route";
    private static final long CONTROL_ROUTE_ID = 1L;

    @Autowired
    private FrontEndRoutingSupportBridgeService frontEndRoutingSupportBridgeService;

    @Test
    public void shouldGetAllRoutes() throws CommunicationFailureException {

        // given
        ExtendedFrontEndRouteDataModel control = getControl(CONTROL_ROUTE, ExtendedFrontEndRouteDataModel.class);

        // when
        ExtendedFrontEndRouteListDataModel result = frontEndRoutingSupportBridgeService.getAllRoutes();

        // then
        assertThat(result, notNullValue());
        assertThat(result.routes().size(), equalTo(NUMBER_OF_ALL_ROUTES));
        assertThat(result.routes().contains(control), is(true));
    }

    @Test
    public void shouldGetRouteByID() throws CommunicationFailureException {

        // given
        ExtendedFrontEndRouteDataModel control = getControl(CONTROL_ROUTE, ExtendedFrontEndRouteDataModel.class);

        // when
        ExtendedFrontEndRouteDataModel result = frontEndRoutingSupportBridgeService.getRouteByID(CONTROL_ROUTE_ID);

        // then
        assertThat(result, equalTo(control));
    }

    @Test
    @ResetDatabase
    public void shouldCreateRoute() throws CommunicationFailureException {

        // given
        FrontEndRouteUpdateRequestModel control = getControl(CONTROL_ROUTE, CONTROL_SUFFIX_CREATE, FrontEndRouteUpdateRequestModel.class);

        // when
        ExtendedFrontEndRouteDataModel result = frontEndRoutingSupportBridgeService.createRoute(control);

        // then
        assertModifiedRoutes(result.id(), control);
    }

    @Test
    @ResetDatabase
    public void shouldUpdateRoute() throws CommunicationFailureException {

        // given
        FrontEndRouteUpdateRequestModel control = getControl(CONTROL_ROUTE, CONTROL_SUFFIX_CREATE, FrontEndRouteUpdateRequestModel.class);

        // when
        ExtendedFrontEndRouteDataModel result = frontEndRoutingSupportBridgeService.updateRoute(CONTROL_ROUTE_ID, control);

        // then
        assertThat(result.id(), equalTo(CONTROL_ROUTE_ID));
        assertModifiedRoutes(result.id(), control);
    }

    @Test
    @ResetDatabase
    public void shouldChangeStatus() throws CommunicationFailureException {

        // given
        // before the test case, make sure the status of the control route item is enabled
        if (!frontEndRoutingSupportBridgeService.getRouteByID(CONTROL_ROUTE_ID).enabled()) {
            fail("Route should be enabled");
        }

        // when
        ExtendedFrontEndRouteDataModel result = frontEndRoutingSupportBridgeService.changeStatus(CONTROL_ROUTE_ID);

        // then
        assertThat(result.enabled(), is(false));
        assertThat(frontEndRoutingSupportBridgeService.getRouteByID(CONTROL_ROUTE_ID).enabled(), is(false));
    }

    @Test
    @ResetDatabase
    public void shouldDeleteRoute() throws CommunicationFailureException {

        // given
        ExtendedFrontEndRouteDataModel control = getControl(CONTROL_ROUTE, ExtendedFrontEndRouteDataModel.class);

        // when
        frontEndRoutingSupportBridgeService.deleteRoute(CONTROL_ROUTE_ID);

        // then
        ExtendedFrontEndRouteListDataModel current = frontEndRoutingSupportBridgeService.getAllRoutes();
        assertThat(current.routes().size(), equalTo(12));
        assertThat(current.routes().contains(control), is(false));
    }

    private void assertModifiedRoutes(Long routeID, FrontEndRouteUpdateRequestModel frontEndRouteUpdateRequestModel) throws CommunicationFailureException {
        ExtendedFrontEndRouteDataModel current = frontEndRoutingSupportBridgeService.getRouteByID(routeID);
        assertThat(current.name(), equalTo(frontEndRouteUpdateRequestModel.getName()));
        assertThat(current.url(), equalTo(frontEndRouteUpdateRequestModel.getUrl()));
        assertThat(current.type(), equalTo(frontEndRouteUpdateRequestModel.getType()));
        assertThat(current.routeId(), equalTo(frontEndRouteUpdateRequestModel.getRouteId()));
        assertThat(current.sequenceNumber(), equalTo(frontEndRouteUpdateRequestModel.getSequenceNumber()));
        assertThat(current.enabled(), is(true));
    }
}
