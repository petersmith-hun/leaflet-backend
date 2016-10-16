package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.annotation.AJAXRequest;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.rest.conversion.ValidationErrorMessagesConverter;
import hu.psprog.leaflet.web.rest.conversion.user.UserCreateRequestModelToUserVOConverter;
import hu.psprog.leaflet.web.rest.conversion.user.UserVOToExtendedUserDataModelEntityConverter;
import hu.psprog.leaflet.web.rest.conversion.user.UserVOToExtendedUserDataModelListConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST controller for user-related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(UsersController.BASE_MAPPING)
public class UsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    static final String BASE_MAPPING = "/users";

    @Autowired
    private UserService userService;

    @Autowired
    private UserVOToExtendedUserDataModelListConverter userVOToExtendedUserDataModelListConverter;

    @Autowired
    private UserVOToExtendedUserDataModelEntityConverter userVOToExtendedUserDataModelEntityConverter;

    @Autowired
    private UserCreateRequestModelToUserVOConverter userCreateRequestModelToUserVOConverter;

    @Autowired
    private ValidationErrorMessagesConverter validationErrorMessagesConverter;

    /**
     * GET /users
     * Returns basic information of all existing users.
     *
     * @return list of existing users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public BaseBodyDataModel getAllUsers() {

        return userVOToExtendedUserDataModelListConverter.convert(userService.getAll());
    }

    /**
     * POST /users
     * Creates a new user. Can be uses only after user database is initialized.
     *
     * @param userCreateRequestModel user data
     * @param bindingResult validation results
     * @return created users data
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @AJAXRequest
    public BaseBodyDataModel createUser(@RequestBody @Valid UserCreateRequestModel userCreateRequestModel, BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationErrorMessagesConverter.convert(bindingResult.getAllErrors());
        } else {
            try {
                Long userID = userService.createOne(userCreateRequestModelToUserVOConverter.convert(userCreateRequestModel));
                UserVO createdUser = userService.getOne(userID);

                return userVOToExtendedUserDataModelEntityConverter.convert(createdUser);
            } catch (ServiceException e) {
                LOGGER.error("Service has thrown an exception. See details:", e);
                throw new RequestCouldNotBeFulfilledException("Your user account could not be created. Please try again later!");
            }
        }
    }
}
