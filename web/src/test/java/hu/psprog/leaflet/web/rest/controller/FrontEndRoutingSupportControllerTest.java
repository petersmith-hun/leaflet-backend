package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteDataModel;
import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteListDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FrontEndRoutingSupportController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndRoutingSupportControllerTest extends AbstractControllerBaseTest {

    private static final long CONTROL_ID = 1L;
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO = FrontEndRouteVO.getBuilder()
            .withId(CONTROL_ID)
            .build();
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST = Collections.singletonList(FRONT_END_ROUTE_VO);
    private static final String LOCATION_HEADER = "/routes/" + CONTROL_ID;

    @Mock
    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @InjectMocks
    private FrontEndRoutingSupportController controller;

    @Before
    public void setup() {
        super.setup();
        given(conversionService.convert(FRONT_END_ROUTE_VO, ExtendedFrontEndRouteDataModel.class)).willReturn(EXTENDED_FRONT_END_ROUTE_DATA_MODEL);
        given(conversionService.convert(FRONT_END_ROUTE_VO_LIST, ExtendedFrontEndRouteListDataModel.class)).willReturn(EXTENDED_FRONT_END_ROUTE_LIST_DATA_MODEL);
        given(conversionService.convert(FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, FrontEndRouteVO.class)).willReturn(FRONT_END_ROUTE_VO);
    }

    @Test
    public void shouldGetAllRoutes() {

        // given
        given(frontEndRoutingSupportFacade.getAll()).willReturn(FRONT_END_ROUTE_VO_LIST);

        // when
        ResponseEntity<ExtendedFrontEndRouteListDataModel> result = controller.getAllRoutes();

        // then
        assertResponse(result, HttpStatus.OK, EXTENDED_FRONT_END_ROUTE_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetRouteByID() throws ResourceNotFoundException, ServiceException {

        // given
        given(frontEndRoutingSupportFacade.getOne(CONTROL_ID)).willReturn(FRONT_END_ROUTE_VO);

        // when
        ResponseEntity<ExtendedFrontEndRouteDataModel> result = controller.getRouteByID(CONTROL_ID);

        // then
        assertResponse(result, HttpStatus.OK, EXTENDED_FRONT_END_ROUTE_DATA_MODEL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldGetRouteByIDThrowResourceNotFoundException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(EntityNotFoundException.class).when(frontEndRoutingSupportFacade).getOne(CONTROL_ID);

        // when
        controller.getRouteByID(CONTROL_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldCreateRoute() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(frontEndRoutingSupportFacade.createOne(FRONT_END_ROUTE_VO)).willReturn(FRONT_END_ROUTE_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createRoute(FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_FRONT_END_ROUTE_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldCreateRouteWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createRoute(FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateRouteWithConstraintViolation() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        doThrow(ConstraintViolationException.class).when(frontEndRoutingSupportFacade).createOne(FRONT_END_ROUTE_VO);

        // when
        controller.createRoute(FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateRouteWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        doThrow(ServiceException.class).when(frontEndRoutingSupportFacade).createOne(FRONT_END_ROUTE_VO);

        // when
        controller.createRoute(FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateRoute() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        given(frontEndRoutingSupportFacade.updateOne(CONTROL_ID, FRONT_END_ROUTE_VO)).willReturn(FRONT_END_ROUTE_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateRoute(CONTROL_ID, FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_FRONT_END_ROUTE_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateRouteWithValidationError() throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateRoute(CONTROL_ID, FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldUpdateRouteWithConstraintViolation() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        doThrow(ConstraintViolationException.class).when(frontEndRoutingSupportFacade).updateOne(CONTROL_ID, FRONT_END_ROUTE_VO);

        // when
        controller.updateRoute(CONTROL_ID, FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldUpdateRouteWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(frontEndRoutingSupportFacade).updateOne(CONTROL_ID, FRONT_END_ROUTE_VO);

        // when
        controller.updateRoute(CONTROL_ID, FRONT_END_ROUTE_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldChangeStatus() throws ServiceException, ResourceNotFoundException {

        // given
        given(frontEndRoutingSupportFacade.changeStatus(CONTROL_ID)).willReturn(FRONT_END_ROUTE_VO);

        // when
        ResponseEntity<ExtendedFrontEndRouteDataModel> result = controller.changeStatus(CONTROL_ID);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_FRONT_END_ROUTE_DATA_MODEL, LOCATION_HEADER);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldChangeStatusWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(frontEndRoutingSupportFacade).changeStatus(CONTROL_ID);

        // when
        controller.changeStatus(CONTROL_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteRoute() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteRoute(CONTROL_ID);

        // then
        verify(frontEndRoutingSupportFacade).deletePermanently(CONTROL_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDeleteRouteWithServiceException() throws ResourceNotFoundException, ServiceException {

        // given
        doThrow(ServiceException.class).when(frontEndRoutingSupportFacade).deletePermanently(CONTROL_ID);

        // when
        controller.deleteRoute(CONTROL_ID);

        // then
        // exception expected
    }
}