package hu.psprog.leaflet.web.rest.controller;

import com.codahale.metrics.annotation.Timed;
import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.UserFacade;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.exception.TokenClaimException;
import hu.psprog.leaflet.web.rest.conversion.user.LoginContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

import static hu.psprog.leaflet.security.jwt.filter.JWTAuthenticationFilter.AUTH_TOKEN_HEADER;

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
    private static final String PATH_CLAIM_TOKEN = "/claim";
    private static final String PATH_REGISTER = "/register";
    private static final String PATH_REVOKE = "/revoke";
    private static final String PATH_IDENTIFIED_USER_UPDATE_PROFILE = PATH_PART_ID + "/profile";
    private static final String PATH_RECLAIM = "/reclaim";
    private static final String PATH_RENEW = "/renew";

    private static final String REQUESTED_USER_IS_NOT_EXISTING = "Requested user is not existing.";
    private static final String USER_COULD_NOT_BE_CREATED = "User could not be created. See details:";
    private static final String USER_ACCOUNT_COULD_NOT_BE_CREATED = "Your user account could not be created. Please try again later!";
    private static final String PROVIDED_EMAIL_ADDRESS_IS_ALREADY_IN_USE = "Provided email address is already in use.";
    private static final String COULD_NOT_REVOKE_TOKEN = "Could not revoke token.";
    private static final String AN_ERROR_OCCURRED_WHILE_SIGNING_YOU_OUT = "An error occurred while signing you out - please try again later.";
    private static final String FAILED_TO_PROCESS_PASSWORD_RESET_REQUEST = "Failed to process password reset request.";
    private static final String UNREGISTERED_EMAIL_ADDRESS = "The email address you've specified could not be found. Please check it.";
    private static final String PASSWORD_RESET_FAILURE = "Your password reset request cannot be processed at the moment - please try again later.";
    private static final String FAILED_TO_EXTEND_USER_SESSION = "Failed to extend user session";

    private UserFacade userFacade;
    private LoginContextFactory loginContextFactory;

    @Autowired
    public UsersController(UserFacade userFacade, LoginContextFactory loginContextFactory) {
        this.userFacade = userFacade;
        this.loginContextFactory = loginContextFactory;
    }

    /**
     * GET /users
     * Returns basic information of all existing users.
     *
     * @return list of existing users.
     */
    @RequestMapping(method = RequestMethod.GET)
    @Timed
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
    @Timed
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
    @Timed
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
    @Timed
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

    /**
     * POST /users/signin
     * Claims token for given existing user.
     *
     * @param loginRequestModel user's email and password
     * @return process status and if "sign-in" is successful, the generated token
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_CLAIM_TOKEN)
    @Timed
    public ResponseEntity<BaseBodyDataModel> claimToken(@RequestBody @Valid LoginRequestModel loginRequestModel,
                                                        HttpServletRequest httpServletRequest, BindingResult bindingResult)
            throws TokenClaimException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                String token = userFacade.login(loginContextFactory.forLogin(loginRequestModel, httpServletRequest));
                return ResponseEntity
                        .ok()
                        .header(AUTH_TOKEN_HEADER, token)
                        .body(LoginResponseDataModel.getBuilder()
                                .withStatus(LoginResponseDataModel.AuthenticationResult.AUTH_SUCCESS)
                                .withToken(token)
                                .build());
            } catch (Exception exc) {
                LOGGER.error("Failed to authenticate user.", exc);
                throw new TokenClaimException();
            }
        }
    }

    /**
     * POST /users/register
     * Public registration endpoints for visitors.
     *
     * @param userInitializeRequestModel user data
     * @param bindingResult validation results
     * @return empty response on success, or validation results on validation failure
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_REGISTER)
    @Timed
    public ResponseEntity<BaseBodyDataModel> signUp(@RequestBody @Valid UserInitializeRequestModel userInitializeRequestModel,
                                    BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                Long userID = userFacade.register(conversionService.convert(userInitializeRequestModel, UserVO.class));
                return ResponseEntity
                        .created(buildLocation(userID))
                        .body(UserDataModel.getBuilder()
                                .withId(userID)
                                .build());
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
     * POST /users/revoke
     * Logout (token revoke) endpoint.
     *
     * @return empty response
     * @throws RequestCouldNotBeFulfilledException is logout could not be performed
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_REVOKE)
    public ResponseEntity<Void> revokeToken() throws RequestCouldNotBeFulfilledException {

        try {
            userFacade.logout();
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (Exception e) {
            LOGGER.error(COULD_NOT_REVOKE_TOKEN, e);
            throw new RequestCouldNotBeFulfilledException(AN_ERROR_OCCURRED_WHILE_SIGNING_YOU_OUT);
        }
    }

    /**
     * POST /users/reclaim
     * Starts password reset process.
     *
     * @param passwordResetDemandRequestModel user's email address wrapped in {@link PasswordResetDemandRequestModel} to request password reset for
     * @param httpServletRequest {@link HttpServletRequest} object
     * @param bindingResult validation result
     * @return empty response on success, validation results if validation fails
     * @throws RequestCouldNotBeFulfilledException if request cannot be processed
     * @throws ResourceNotFoundException if no existing user found for given email address
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_RECLAIM)
    public ResponseEntity<BaseBodyDataModel> demandPasswordReset(@RequestBody @Valid PasswordResetDemandRequestModel passwordResetDemandRequestModel,
                                                    HttpServletRequest httpServletRequest, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                userFacade.demandPasswordReset(loginContextFactory.forPasswordReset(passwordResetDemandRequestModel, httpServletRequest));
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .build();
            } catch (UsernameNotFoundException e) {
                LOGGER.error(FAILED_TO_PROCESS_PASSWORD_RESET_REQUEST, e);
                throw new ResourceNotFoundException(UNREGISTERED_EMAIL_ADDRESS);
            } catch (Exception e) {
                LOGGER.error(FAILED_TO_PROCESS_PASSWORD_RESET_REQUEST, e);
                throw new RequestCouldNotBeFulfilledException(PASSWORD_RESET_FAILURE);
            }
        }
    }

    /**
     * PUT /users/reclaim
     * Confirms password reset request by providing the new password.
     *
     * @param userPasswordRequestModel {@link UserPasswordRequestModel} holding the new password and its confirmation
     * @param bindingResult validation result
     * @return empty response on success, validation results if validation fails
     * @throws RequestCouldNotBeFulfilledException if request cannot be processed
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_RECLAIM)
    public ResponseEntity<BaseBodyDataModel> confirmPasswordReset(@RequestBody @Valid UserPasswordRequestModel userPasswordRequestModel,
                                                                  BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                userFacade.confirmPasswordReset(userPasswordRequestModel.getPassword());
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .build();
            } catch (Exception e) {
                LOGGER.error(FAILED_TO_PROCESS_PASSWORD_RESET_REQUEST, e);
                throw new RequestCouldNotBeFulfilledException(PASSWORD_RESET_FAILURE);
            }
        }
    }

    /**
     * PUT /users/renew
     * Claims "session extension" - replaces given token with a new one.
     *
     * @param httpServletRequest {@link HttpServletRequest} object
     * @return new token wrapped in {@link LoginResponseDataModel}
     * @throws RequestCouldNotBeFulfilledException if request cannot be processed
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_RENEW)
    public ResponseEntity<LoginResponseDataModel> renewToken(HttpServletRequest httpServletRequest) throws RequestCouldNotBeFulfilledException {

        try {
            String token = userFacade.extendSession(loginContextFactory.forRenewal(httpServletRequest));

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(LoginResponseDataModel.getBuilder()
                            .withStatus(LoginResponseDataModel.AuthenticationResult.AUTH_SUCCESS)
                            .withToken(token)
                            .build());
        } catch (Exception e) {
            LOGGER.error(FAILED_TO_EXTEND_USER_SESSION, e);
            throw new RequestCouldNotBeFulfilledException(FAILED_TO_EXTEND_USER_SESSION);
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_USERS + "/" + id);
    }
}
