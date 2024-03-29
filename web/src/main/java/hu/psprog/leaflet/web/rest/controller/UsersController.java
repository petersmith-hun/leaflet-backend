package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.UserFacade;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.metrics.ExceptionHandlerCounters;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.net.URI;

/**
 * REST controller for user-related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_USERS)
public class UsersController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    private static final String PATH_IDENTIFIED_USER_UPDATE_ROLE = PATH_PART_ID + "/role";
    private static final String PATH_IDENTIFIED_USER_UPDATE_PASSWORD = PATH_PART_ID + "/password";
    private static final String PATH_IDENTIFIED_USER_UPDATE_PROFILE = PATH_PART_ID + "/profile";

    private static final String REQUESTED_USER_IS_NOT_EXISTING = "Requested user is not existing.";
    private static final String USER_COULD_NOT_BE_CREATED = "User could not be created. See details:";
    private static final String USER_ACCOUNT_COULD_NOT_BE_CREATED = "Your user account could not be created. Please try again later!";
    private static final String PROVIDED_EMAIL_ADDRESS_IS_ALREADY_IN_USE = "Provided email address is already in use.";

    private final UserFacade userFacade;

    @Autowired
    public UsersController(ConversionService conversionService, ExceptionHandlerCounters exceptionHandlerCounters,
                           UserFacade userFacade) {
        super(conversionService, exceptionHandlerCounters);
        this.userFacade = userFacade;
    }

    /**
     * GET /users
     * Returns basic information of all existing users.
     *
     * @return list of existing users.
     */
    @RequestMapping(method = RequestMethod.GET)
    @Timed(value = "getAllUsers", extraTags = {"controller", "users"})
    public ResponseEntity<UserListDataModel> getAllUsers() {

        return ResponseEntity
                .ok()
                .body(conversionService.convert(userFacade.getUserList(), UserListDataModel.class));
    }

    /**
     * POST /users
     * Creates a new user. Can be uses only after user database is initialized.
     *
     * @param userCreateRequestModel user data
     * @param bindingResult validation results
     * @return created user's data
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseBodyDataModel> createUser(@RequestBody @Valid UserCreateRequestModel userCreateRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                UserVO createdUser = userFacade.createUser(conversionService.convert(userCreateRequestModel, UserVO.class));
                return ResponseEntity
                        .created(buildLocation(createdUser.getId()))
                        .body(conversionService.convert(createdUser, ExtendedUserDataModel.class));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(PROVIDED_EMAIL_ADDRESS_IS_ALREADY_IN_USE);
            } catch (ServiceException e) {
                LOGGER.error(USER_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(USER_ACCOUNT_COULD_NOT_BE_CREATED);
            }
        }
    }

    /**
     * GET /users/{id}
     * Returns user identified by given ID.
     *
     * @param id ID of an existing user
     * @return user's data
     * @throws ResourceNotFoundException when no user exists identified by the given ID
     */
    @RequestMapping(method = RequestMethod.GET, path = PATH_PART_ID)
    @Timed(value = "getUserByID", extraTags = {"controller", "users"})
    public ResponseEntity<ExtendedUserDataModel> getUserByID(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(userFacade.getUserByID(id), ExtendedUserDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
            throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
        }
    }

    /**
     * DELETE /users/{id}
     *
     * @param id ID of an existing user
     * @throws ResourceNotFoundException when no user exists identified by the given ID
     */
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_PART_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            userFacade.deleteUserByID(id);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
            throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
        }
    }

    /**
     * PUT /users/{id}/role
     * Updates given user's role.
     *
     * @param id ID of an existing user
     * @param updateRoleRequestModel new role name
     * @return updated data of given user
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_IDENTIFIED_USER_UPDATE_ROLE)
    public ResponseEntity<BaseBodyDataModel> updateRole(@PathVariable(PATH_VARIABLE_ID) Long id,
                                   @RequestBody @Valid UpdateRoleRequestModel updateRoleRequestModel,
                                   BindingResult bindingResult)
            throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                UserVO userVO = userFacade.changeAuthority(id, updateRoleRequestModel.getRole());
                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(userVO, ExtendedUserDataModel.class));
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
                throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
            }
        }
    }

    /**
     * PUT /users/{id}/profile
     * Updates given user's changeable profile fields.
     *
     * @param id ID of an existing user
     * @param updateProfileRequestModel new email, username and locale values
     * @return updated data of given user
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_IDENTIFIED_USER_UPDATE_PROFILE)
    @Timed(value = "updateProfile", extraTags = {"controller", "users"})
    public ResponseEntity<BaseBodyDataModel> updateProfile(@PathVariable(PATH_VARIABLE_ID) Long id,
                                      @RequestBody @Valid UpdateProfileRequestModel updateProfileRequestModel,
                                      BindingResult bindingResult)
            throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                UserVO userVO = userFacade.updateUserProfile(id, conversionService.convert(updateProfileRequestModel, UserVO.class));
                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(userVO, ExtendedUserDataModel.class));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(PROVIDED_EMAIL_ADDRESS_IS_ALREADY_IN_USE);
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
                throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
            }
        }
    }

    /**
     * PUT /users/{id}/password
     * Updates given user's password.
     *
     * @param id ID of an existing user
     * @param passwordChangeRequestModel current password for permission validation and new password with its confirmation
     * @return updated data of given user
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_IDENTIFIED_USER_UPDATE_PASSWORD)
    @Timed(value = "updatePassword", extraTags = {"controller", "users"})
    public ResponseEntity<BaseBodyDataModel> updatePassword(@PathVariable(PATH_VARIABLE_ID) Long id,
                                       @RequestBody @Valid PasswordChangeRequestModel passwordChangeRequestModel,
                                       BindingResult bindingResult)
            throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                UserVO userVO = userFacade.updateUserPassword(id, passwordChangeRequestModel.getCurrentPassword(), passwordChangeRequestModel.getPassword());
                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(userVO, ExtendedUserDataModel.class));
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
                throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
            }
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(String.format("%s/%s", BASE_PATH_USERS, id));
    }
}
