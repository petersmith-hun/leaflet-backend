package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.vo.AuthRequestVO;
import hu.psprog.leaflet.service.vo.AuthResponseVO;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.exception.TokenClaimException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    private static final String PATH_INIT = "/init";
    private static final String PATH_IDENTIFIED_USER_UPDATE_ROLE = PATH_PART_ID + "/role";
    private static final String PATH_IDENTIFIED_USER_UPDATE_PASSWORD = PATH_PART_ID + "/password";
    private static final String PATH_CLAIM_TOKEN = "/claim";
    private static final String PATH_REGISTER = "/register";
    private static final String PATH_IDENTIFIED_USER_UPDATE_PROFILE = PATH_PART_ID + "/profile";

    private static final String REQUESTED_USER_IS_NOT_EXISTING = "Requested user is not existing.";
    private static final String INITIALIZATION_FAILED_SEE_DETAILS = "Initialization failed. See details:";
    private static final String INITIALIZATION_IS_NOT_AVAILABLE_NOW = "Initialization is not available now.";
    private static final String SERVICE_HAS_THROWN_AN_EXCEPTION = "Service has thrown an exception. See details:";
    private static final String USER_COULD_NOT_BE_CREATED = "User could not be created. See details:";
    private static final String USER_ACCOUNT_COULD_NOT_BE_CREATED = "Your user account could not be created. Please try again later!";
    private static final String PROVIDED_EMAIL_ADDRESS_IS_ALREADY_IN_USE = "Provided email address is already in use.";
    private static final String LAST_LOGIN_COULD_NOT_BE_UPDATED_FOR_THIS_USER = "Last login could not be updated for this user.";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * GET /users
     * Returns basic information of all existing users.
     *
     * @return list of existing users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<UserListDataModel> getAllUsers() {

        return ResponseEntity
                .ok()
                .body(conversionService.convert(userService.getAll(), UserListDataModel.class));
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
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                hashPassword(userCreateRequestModel);
                Long userID = userService.createOne(conversionService.convert(userCreateRequestModel, UserVO.class));
                UserVO createdUser = userService.getOne(userID);

                return ResponseEntity
                        .created(buildLocation(userID))
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
     * POST /users/init
     * Initializes user database with the first user who always has ADMIN role. After initialization, endpoint shall not be used!
     *
     * @param userInitializeRequestModel user data
     * @param bindingResult validation results
     * @return created user's data
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_INIT)
    public ResponseEntity<BaseBodyDataModel> initUserDatabase(@RequestBody @Valid UserInitializeRequestModel userInitializeRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                hashPassword(userInitializeRequestModel);
                Long userID = userService.initialize(conversionService.convert(userInitializeRequestModel, UserVO.class));
                UserVO createdUser = userService.getOne(userID);

                return ResponseEntity
                        .created(buildLocation(userID))
                        .body(conversionService.convert(createdUser, ExtendedUserDataModel.class));
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
    @RequestMapping(method = RequestMethod.GET, path = PATH_PART_ID)
    public ResponseEntity<ExtendedUserDataModel> getUserByID(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            UserVO userVO = userService.getOne(id);

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(userVO, ExtendedUserDataModel.class));
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
    public ResponseEntity<BaseBodyDataModel> updateRole(@PathVariable(PATH_VARIABLE_ID) Long id,
                                   @RequestBody @Valid UpdateRoleRequestModel updateRoleRequestModel,
                                   BindingResult bindingResult)
            throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                userService.changeAuthority(id, Authority.getAuthorityByName(updateRoleRequestModel.getRole()));
                UserVO userVO = userService.getOne(id);

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
    public ResponseEntity<BaseBodyDataModel> updateProfile(@PathVariable(PATH_VARIABLE_ID) Long id,
                                      @RequestBody @Valid UpdateProfileRequestModel updateProfileRequestModel,
                                      BindingResult bindingResult)
            throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                userService.updateOne(id, conversionService.convert(updateProfileRequestModel, UserVO.class));
                UserVO userVO = userService.getOne(id);

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
     * @param userPasswordRequestModel new password and its confirmation
     * @return updated data of given user
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_IDENTIFIED_USER_UPDATE_PASSWORD)
    public ResponseEntity<BaseBodyDataModel> updatePassword(@PathVariable(PATH_VARIABLE_ID) Long id,
                                       @RequestBody @Valid UserPasswordRequestModel userPasswordRequestModel,
                                       BindingResult bindingResult)
            throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                hashPassword(userPasswordRequestModel);
                userService.changePassword(id, userPasswordRequestModel.getPassword());
                UserVO userVO = userService.getOne(id);

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
    public ResponseEntity<BaseBodyDataModel> claimToken(@RequestBody @Valid LoginRequestModel loginRequestModel, BindingResult bindingResult) throws TokenClaimException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            AuthRequestVO requestModel = conversionService.convert(loginRequestModel, AuthRequestVO.class);
            AuthResponseVO authenticationAnswer = userService.claimToken(requestModel);

            if (authenticationAnswer.getAuthenticationResult() == AuthResponseVO.AuthenticationResult.INVALID_CREDENTIALS) {
                throw new TokenClaimException();
            }

            try {
                userService.updateLastLogin(loginRequestModel.getEmail());
            } catch (EntityNotFoundException e) {
                LOGGER.error(REQUESTED_USER_IS_NOT_EXISTING, e);
                throw new ResourceNotFoundException(LAST_LOGIN_COULD_NOT_BE_UPDATED_FOR_THIS_USER);
            }

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(authenticationAnswer, LoginResponseDataModel.class));
        }
    }

    /**
     * POST /users/register
     * Public registration endpoints for visitors.
     *
     * @param userInitializeRequestModel user data
     * @param bindingResult validation results
     * @return created user's data
     */
    @RequestMapping(method = RequestMethod.POST, path = PATH_REGISTER)
    public ResponseEntity<BaseBodyDataModel> signUp(@RequestBody @Valid UserInitializeRequestModel userInitializeRequestModel,
                                    BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                hashPassword(userInitializeRequestModel);
                Long userID = userService.createOne(conversionService.convert(userInitializeRequestModel, UserVO.class));
                UserVO createdUser = userService.getOne(userID);

                return ResponseEntity
                        .ok()
                        .body(conversionService.convert(createdUser, ExtendedUserDataModel.class));
            } catch (ServiceException e) {
                LOGGER.error(USER_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(USER_ACCOUNT_COULD_NOT_BE_CREATED);
            }
        }
    }

    private void hashPassword(UserPasswordRequestModel userPasswordRequestModel) {
        String encodedPassword = passwordEncoder.encode(userPasswordRequestModel.getPassword());
        userPasswordRequestModel.setPassword(encodedPassword);
        userPasswordRequestModel.setPasswordConfirmation(null); // we don't need confirmation value anymore
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_USERS + "/" + id);
    }
}
