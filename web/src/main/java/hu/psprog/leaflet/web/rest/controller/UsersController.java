package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.vo.AuthRequestVO;
import hu.psprog.leaflet.service.vo.AuthResponseVO;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.annotation.AJAXRequest;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.exception.TokenClaimException;
import hu.psprog.leaflet.web.rest.conversion.ValidationErrorMessagesConverter;
import hu.psprog.leaflet.web.rest.conversion.user.AuthResponseVOToLoginResponseDataModelConverter;
import hu.psprog.leaflet.web.rest.conversion.user.LoginRequestModelToAuthenticationRequestVOConverter;
import hu.psprog.leaflet.web.rest.conversion.user.UpdateProfileRequestModelToUserVOConverter;
import hu.psprog.leaflet.web.rest.conversion.user.UserInitializeRequestModelToUserVOConverter;
import hu.psprog.leaflet.web.rest.conversion.user.UserVOToExtendedUserDataModelEntityConverter;
import hu.psprog.leaflet.web.rest.conversion.user.UserVOToExtendedUserDataModelListConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
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

    static final String BASE_MAPPING = "/users";

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    private static final String PATH_VARIABLE_USER_ID = "id";
    private static final String PATH_INIT = "/init";
    private static final String PATH_IDENTIFIED_USER = "/{id}";
    private static final String PATH_IDENTIFIED_USER_UPDATE_ROLE = "/{id}/role";
    private static final String PATH_IDENTIFIED_USER_UPDATE_PASSWORD = "/{id}/password";
    private static final String PATH_CLAIM_TOKEN = "/claim";
    private static final String PATH_IDENTIFIED_USER_UPDATE_PROFILE = "/{id}/profile";

    private static final String REQUESTED_USER_IS_NOT_EXISTING = "Requested user is not existing.";
    private static final String INITIALIZATION_FAILED_SEE_DETAILS = "Initialization failed. See details:";
    private static final String INITIALIZATION_IS_NOT_AVAILABLE_NOW = "Initialization is not available now.";
    private static final String SERVICE_HAS_THROWN_AN_EXCEPTION = "Service has thrown an exception. See details:";
    private static final String USER_COULD_NOT_BE_CREATED = "User could not be created. See details:";
    private static final String USER_ACCOUNT_COULD_NOT_BE_CREATED = "Your user account could not be created. Please try again later!";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserVOToExtendedUserDataModelListConverter userVOToExtendedUserDataModelListConverter;

    @Autowired
    private UserVOToExtendedUserDataModelEntityConverter userVOToExtendedUserDataModelEntityConverter;

    @Autowired
    private UserInitializeRequestModelToUserVOConverter userInitializeRequestModelToUserVOConverter;

    @Autowired
    private UpdateProfileRequestModelToUserVOConverter updateProfileRequestModelToUserVOConverter;

    @Autowired
    private LoginRequestModelToAuthenticationRequestVOConverter loginRequestModelToAuthenticationRequestVOConverter;

    @Autowired
    private AuthResponseVOToLoginResponseDataModelConverter authResponseVOToLoginResponseDataModelConverter;

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
     * @return created user's data
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @AJAXRequest
    public BaseBodyDataModel createUser(@RequestBody @Valid UserCreateRequestModel userCreateRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationErrorMessagesConverter.convert(bindingResult.getAllErrors());
        } else {
            try {
                hashPassword(userCreateRequestModel);
                Long userID = userService.createOne(userInitializeRequestModelToUserVOConverter.convert(userCreateRequestModel));
                UserVO createdUser = userService.getOne(userID);

                return userVOToExtendedUserDataModelEntityConverter.convert(createdUser);
            } catch (ServiceException e) {
                LOGGER.error(USER_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(USER_ACCOUNT_COULD_NOT_BE_CREATED);
            }
        }
    }

    /**
     * POST /users/init
     * Initializes user database with the first user who always has ADMIN role. After initialization, endpoint shall not be used!
     *
     * @param userInitializeRequestModel user data
     * @param bindingResult validation results
     * @return created user's data
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_INIT)
    @ResponseStatus(HttpStatus.CREATED)
    @AJAXRequest
    public BaseBodyDataModel initUserDatabase(@RequestBody @Valid UserInitializeRequestModel userInitializeRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationErrorMessagesConverter.convert(bindingResult.getAllErrors());
        } else {
            try {
                Long userID = userService.initialize(userInitializeRequestModelToUserVOConverter.convert(userInitializeRequestModel));
                UserVO createdUser = userService.getOne(userID);

                return userVOToExtendedUserDataModelEntityConverter.convert(createdUser);
            } catch (UserInitializationException e) {
                LOGGER.error(SERVICE_HAS_THROWN_AN_EXCEPTION, e);
                throw new RequestCouldNotBeFulfilledException(INITIALIZATION_IS_NOT_AVAILABLE_NOW, e);
            } catch (EntityCreationException e) {
                LOGGER.error("User could not be created. See details:", e);
                throw new RequestCouldNotBeFulfilledException(INITIALIZATION_FAILED_SEE_DETAILS, e);
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
                throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
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
    @RequestMapping(method = RequestMethod.GET, path = PATH_IDENTIFIED_USER)
    public BaseBodyDataModel getUserByID(@PathVariable(PATH_VARIABLE_USER_ID) Long id) throws ResourceNotFoundException {

        try {
            UserVO userVO = userService.getOne(id);

            return userVOToExtendedUserDataModelEntityConverter.convert(userVO);
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
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_IDENTIFIED_USER)
    public void deleteUser(@PathVariable(PATH_VARIABLE_USER_ID) Long id) throws ResourceNotFoundException {

        try {
            UserVO userVO = userService.getOne(id);
            userService.deleteByEntity(userVO);
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
    @ResponseStatus(HttpStatus.CREATED)
    public BaseBodyDataModel updateRole(@PathVariable(PATH_VARIABLE_USER_ID) Long id,
                                        @RequestBody @Valid UpdateRoleRequestModel updateRoleRequestModel)
            throws ResourceNotFoundException {

        try {
            userService.changeAuthority(id, Authority.getAuthorityByName(updateRoleRequestModel.getRole()));
            UserVO userVO = userService.getOne(id);

            return userVOToExtendedUserDataModelEntityConverter.convert(userVO);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
            throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
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
    @ResponseStatus(HttpStatus.CREATED)
    public BaseBodyDataModel updateProfile(@PathVariable(PATH_VARIABLE_USER_ID) Long id,
                                           @RequestBody @Valid UpdateProfileRequestModel updateProfileRequestModel)
            throws ResourceNotFoundException {

        try {
            userService.updateOne(id, updateProfileRequestModelToUserVOConverter.convert(updateProfileRequestModel));
            UserVO userVO = userService.getOne(id);

            return userVOToExtendedUserDataModelEntityConverter.convert(userVO);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
            throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
        }
    }

    /**
     * PUT /users/{id}/password
     * Updates given user's password.
     *
     * @param id ID of an existing user
     * @param userPasswordRequestModel new password and its confirmation
     * @return updated data of given user
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_IDENTIFIED_USER_UPDATE_PASSWORD)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseBodyDataModel updatePassword(@PathVariable(PATH_VARIABLE_USER_ID) Long id,
                                            @RequestBody @Valid UserPasswordRequestModel userPasswordRequestModel)
            throws ResourceNotFoundException {

        try {
            hashPassword(userPasswordRequestModel);
            userService.changePassword(id, userPasswordRequestModel.getPassword());
            UserVO userVO = userService.getOne(id);

            return userVOToExtendedUserDataModelEntityConverter.convert(userVO);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
            throw new ResourceNotFoundException(REQUESTED_USER_IS_NOT_EXISTING);
        }
    }

    /**
     * POST /users/signin
     * Claims token for given existing user.
     *
     * @param loginRequestModel user's email and password
     * @return process status and if "sign-in" is successful, the generated token
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_CLAIM_TOKEN)
    public BaseBodyDataModel claimToken(@RequestBody @Valid LoginRequestModel loginRequestModel) throws TokenClaimException {

        AuthRequestVO requestModel = loginRequestModelToAuthenticationRequestVOConverter.convert(loginRequestModel);
        AuthResponseVO authenticationAnswer = userService.claimToken(requestModel);

        if (authenticationAnswer.getAuthenticationResult() == AuthResponseVO.AuthenticationResult.INVALID_CREDENTIALS) {
            throw new TokenClaimException();
        }

        return authResponseVOToLoginResponseDataModelConverter.convert(authenticationAnswer);
    }

    private void hashPassword(UserPasswordRequestModel userPasswordRequestModel) {
        String encodedPassword = passwordEncoder.encode(userPasswordRequestModel.getPassword());
        userPasswordRequestModel.setPassword(encodedPassword);
        userPasswordRequestModel.setPasswordConfirmation(null); // we don't need confirmation value anymore
    }
}
