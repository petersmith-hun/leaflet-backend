package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.routing.FrontEndRouteUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteDataModel;
import hu.psprog.leaflet.api.rest.response.routing.ExtendedFrontEndRouteListDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * REST controller for front-end routing support operations.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_ROUTES)
public class FrontEndRoutingSupportController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndRoutingSupportController.class);

    private static final String REQUESTED_ROUTE_NOT_FOUND = "Requested route not found.";
    private static final String ROUTE_COULD_NOT_BE_CREATED = "Route could not be created";
    private static final String THE_REQUESTED_ROUTE_ITEM_YOU_ARE_LOOKING_FOR_DOES_NOT_EXIST = "The requested route item you are looking for does not exist.";
    private static final String ROUTE_COULD_NOT_BE_CREATED_PLEASE_TRY_AGAIN_LATER = "Route could not be created, please try again later.";
    private static final String A_ROUTE_WITH_THE_SPECIFIED_ID_ALREADY_EXISTS = "A route with the specified ID already exists";

    private FrontEndRoutingSupportFacade frontEndRoutingSupportFacade;

    @Autowired
    public FrontEndRoutingSupportController(FrontEndRoutingSupportFacade frontEndRoutingSupportFacade) {
        this.frontEndRoutingSupportFacade = frontEndRoutingSupportFacade;
    }

    /**
     * GET /routes
     * Returns all existing route items.
     *
     * @return list of existing route items.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ExtendedFrontEndRouteListDataModel> getAllRoutes() {

        List<FrontEndRouteVO> frontEndRouteVOList = frontEndRoutingSupportFacade.getAll();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(frontEndRouteVOList, ExtendedFrontEndRouteListDataModel.class));
    }

    /**
     * GET /routes/{id}
     * Returns route item identified by given ID.
     *
     * @param id ID of the route item
     * @return identified route item
     * @throws ResourceNotFoundException if no route item found identified by given ID
     */
    @RequestMapping(method = RequestMethod.GET, path = PATH_PART_ID)
    public ResponseEntity<ExtendedFrontEndRouteDataModel> getRouteByID(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            FrontEndRouteVO routeVO = frontEndRoutingSupportFacade.getOne(id);

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(routeVO, ExtendedFrontEndRouteDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_ROUTE_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_REQUESTED_ROUTE_ITEM_YOU_ARE_LOOKING_FOR_DOES_NOT_EXIST);
        }
    }

    /**
     * POST /routes
     * Creates a new route item.
     *
     * @param frontEndRouteUpdateRequestModel {@link FrontEndRouteUpdateRequestModel} containing route information
     * @param bindingResult validation results
     * @return data of created route item
     * @throws RequestCouldNotBeFulfilledException if route item cannot be created
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseBodyDataModel> createRoute(@RequestBody @Valid FrontEndRouteUpdateRequestModel frontEndRouteUpdateRequestModel,
                                                         BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            FrontEndRouteVO routeVOToSave = conversionService.convert(frontEndRouteUpdateRequestModel, FrontEndRouteVO.class);
            try {
                FrontEndRouteVO createdRouteVO = frontEndRoutingSupportFacade.createOne(routeVOToSave);

                return ResponseEntity
                        .created(buildLocation(createdRouteVO.getId()))
                        .body(conversionService.convert(createdRouteVO, ExtendedFrontEndRouteDataModel.class));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(A_ROUTE_WITH_THE_SPECIFIED_ID_ALREADY_EXISTS);
            } catch (ServiceException e) {
                LOGGER.error(ROUTE_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(ROUTE_COULD_NOT_BE_CREATED_PLEASE_TRY_AGAIN_LATER);
            }
        }
    }

    /**
     * PUT /routes/{id}
     * Updates an existing route item.
     *
     * @param id ID of the route item
     * @param frontEndRouteUpdateRequestModel {@link FrontEndRouteUpdateRequestModel} containing updated route information
     * @param bindingResult validation result
     * @return updated data of route item
     * @throws ResourceNotFoundException if no route item found identified by given ID
     * @throws RequestCouldNotBeFulfilledException if route item cannot be updated
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_PART_ID)
    public ResponseEntity<BaseBodyDataModel> updateRoute(@PathVariable(PATH_VARIABLE_ID) Long id,
                                                         @RequestBody @Valid FrontEndRouteUpdateRequestModel frontEndRouteUpdateRequestModel,
                                                         BindingResult bindingResult)
            throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            FrontEndRouteVO routeVOToUpdate = conversionService.convert(frontEndRouteUpdateRequestModel, FrontEndRouteVO.class);
            try {
                FrontEndRouteVO updatedRouteVO = frontEndRoutingSupportFacade.updateOne(id, routeVOToUpdate);

                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(updatedRouteVO, ExtendedFrontEndRouteDataModel.class));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(A_ROUTE_WITH_THE_SPECIFIED_ID_ALREADY_EXISTS);
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_ROUTE_NOT_FOUND, e);
                throw new ResourceNotFoundException(THE_REQUESTED_ROUTE_ITEM_YOU_ARE_LOOKING_FOR_DOES_NOT_EXIST);
            }
        }
    }

    /**
     * PUT /routes/{id}/status
     * Changes route item status (enabled/disabled).
     *
     * @param id ID of the route item
     * @return updated data of route item
     * @throws ResourceNotFoundException if no route item found identified by given ID
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_CHANGE_STATUS)
    public ResponseEntity<ExtendedFrontEndRouteDataModel> changeStatus(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            FrontEndRouteVO updatedRouteVO = frontEndRoutingSupportFacade.changeStatus(id);

            return ResponseEntity
                    .created(buildLocation(id))
                    .body(conversionService.convert(updatedRouteVO, ExtendedFrontEndRouteDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_ROUTE_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_REQUESTED_ROUTE_ITEM_YOU_ARE_LOOKING_FOR_DOES_NOT_EXIST);
        }
    }

    /**
     * DELETE /routes/{id}
     * Deletes route item identified by given ID.
     *
     * @param id ID of the route item
     * @throws ResourceNotFoundException if no route item found identified by given ID
     */
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_PART_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoute(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            frontEndRoutingSupportFacade.deletePermanently(id);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_ROUTE_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_REQUESTED_ROUTE_ITEM_YOU_ARE_LOOKING_FOR_DOES_NOT_EXIST);
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_ROUTES + "/" + id);
    }
}
