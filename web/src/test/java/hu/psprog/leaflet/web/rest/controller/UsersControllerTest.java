package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.UserFacade;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.exception.TokenClaimException;
import hu.psprog.leaflet.web.rest.conversion.user.LoginContextFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

import static hu.psprog.leaflet.security.jwt.filter.JWTAuthenticationFilter.AUTH_TOKEN_HEADER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UsersController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest extends AbstractControllerBaseTest {

    private static final List<UserVO> USER_VO_LIST = Collections.singletonList(USER_VO);
    private static final long USER_ID = 1L;
    private static final String LOCATION_HEADER = "/users/" + USER_ID;

    @Mock
    private UserFacade userFacade;

    @Mock
    private LoginContextFactory loginContextFactory;

    @InjectMocks
    private UsersController controller;

    @Before
    public void setup() {
        super.setup();
        given(conversionService.convert(USER_VO_LIST, UserListDataModel.class)).willReturn(USER_LIST_DATA_MODEL);
        given(conversionService.convert(USER_VO, ExtendedUserDataModel.class)).willReturn(EXTENDED_USER_DATA_MODEL);
    }

    @Test
    public void shouldGetAllUsers() {

        // given
        given(userFacade.getUserList()).willReturn(USER_VO_LIST);

        // when
        ResponseEntity<UserListDataModel> result = controller.getAllUsers();

        // then
        assertResponse(result, HttpStatus.OK, USER_LIST_DATA_MODEL);
    }

    @Test
    public void shouldCreateUser() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(USER_CREATE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        given(userFacade.createUser(USER_VO)).willReturn(USER_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createUser(USER_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_USER_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldCreateUserWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createUser(USER_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateUserWithConstraintViolation() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(USER_CREATE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ConstraintViolationException.class).when(userFacade).createUser(USER_VO);

        // when
        controller.createUser(USER_CREATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateUserWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(USER_CREATE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ServiceException.class).when(userFacade).createUser(USER_VO);

        // when
        controller.createUser(USER_CREATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldGetUserByID() throws ServiceException, ResourceNotFoundException {

        // given
        given(userFacade.getUserByID(USER_ID)).willReturn(USER_VO);

        // when
        ResponseEntity<ExtendedUserDataModel> result = controller.getUserByID(USER_ID);

        // then
        assertResponse(result, HttpStatus.OK, EXTENDED_USER_DATA_MODEL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldGetUserByIDWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(userFacade).getUserByID(USER_ID);

        // when
        controller.getUserByID(USER_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteUser() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteUser(USER_ID);

        // then
        verify(userFacade).deleteUserByID(USER_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDeleteUserWithServiceException() throws ResourceNotFoundException, ServiceException {

        // given
        doThrow(ServiceException.class).when(userFacade).deleteUserByID(USER_ID);

        // when
        controller.deleteUser(USER_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateRole() throws ServiceException, ResourceNotFoundException {

        // given
        UpdateRoleRequestModel updateRoleRequestModel = prepareUpdateRoleRequestModel();
        given(userFacade.changeAuthority(USER_ID, updateRoleRequestModel.getRole())).willReturn(USER_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateRole(USER_ID, updateRoleRequestModel, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_USER_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateRoleWithValidationError() throws ResourceNotFoundException {

        // given
        UpdateRoleRequestModel updateRoleRequestModel = prepareUpdateRoleRequestModel();
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateRole(USER_ID, updateRoleRequestModel, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldUpdateRoleWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        UpdateRoleRequestModel updateRoleRequestModel = prepareUpdateRoleRequestModel();
        doThrow(ServiceException.class).when(userFacade).changeAuthority(USER_ID, updateRoleRequestModel.getRole());

        // when
        controller.updateRole(USER_ID, updateRoleRequestModel, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateProfile() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException, ServiceException {

        // given
        given(conversionService.convert(UPDATE_PROFILE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        given(userFacade.updateUserProfile(USER_ID, USER_VO)).willReturn(USER_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_USER_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateProfileWithValidationError() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldUpdateProfileWithConstraintViolation() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException, ServiceException {

        // given
        given(conversionService.convert(UPDATE_PROFILE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ConstraintViolationException.class).when(userFacade).updateUserProfile(USER_ID, USER_VO);

        // when
        controller.updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldUpdateProfileWithServiceException() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException, ServiceException {

        // given
        given(conversionService.convert(UPDATE_PROFILE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ServiceException.class).when(userFacade).updateUserProfile(USER_ID, USER_VO);

        // when
        controller.updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdatePassword() throws ServiceException, ResourceNotFoundException {

        // given
        PasswordChangeRequestModel passwordChangeRequestModel = preparePasswordChangeRequestModel();
        given(userFacade.updateUserPassword(USER_ID, passwordChangeRequestModel.getCurrentPassword(), passwordChangeRequestModel.getPassword())).willReturn(USER_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updatePassword(USER_ID, passwordChangeRequestModel, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_USER_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdatePasswordWithValidationError() throws ResourceNotFoundException {

        // given
        PasswordChangeRequestModel passwordChangeRequestModel = preparePasswordChangeRequestModel();
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updatePassword(USER_ID, passwordChangeRequestModel, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldUpdatePasswordWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        PasswordChangeRequestModel passwordChangeRequestModel = preparePasswordChangeRequestModel();
        doThrow(ServiceException.class).when(userFacade).updateUserPassword(USER_ID, passwordChangeRequestModel.getCurrentPassword(), passwordChangeRequestModel.getPassword());

        // when
        controller.updatePassword(USER_ID, passwordChangeRequestModel, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldClaimToken() throws EntityNotFoundException, TokenClaimException {

        // given
        given(loginContextFactory.forLogin(LOGIN_REQUEST_MODEL, httpServletRequest)).willReturn(LOGIN_CONTEXT_VO_FOR_LOGIN);
        given(userFacade.login(LOGIN_CONTEXT_VO_FOR_LOGIN)).willReturn(TOKEN);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.claimToken(LOGIN_REQUEST_MODEL, httpServletRequest, bindingResult);

        // then
        assertResponse(result, HttpStatus.OK, LOGIN_RESPONSE_DATA_MODEL_WITH_SUCCESS);
        assertThat(result.getHeaders().get(AUTH_TOKEN_HEADER).get(0), equalTo(TOKEN));
    }

    @Test
    public void shouldClaimTokenWithValidationError() throws TokenClaimException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.claimToken(LOGIN_REQUEST_MODEL, httpServletRequest, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = TokenClaimException.class)
    public void shouldClaimTokenWithServiceException() throws EntityNotFoundException, TokenClaimException {

        // given
        given(loginContextFactory.forLogin(LOGIN_REQUEST_MODEL, httpServletRequest)).willReturn(LOGIN_CONTEXT_VO_FOR_LOGIN);
        doThrow(ServiceException.class).when(userFacade).login(LOGIN_CONTEXT_VO_FOR_LOGIN);

        // when
        controller.claimToken(LOGIN_REQUEST_MODEL, httpServletRequest, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldSignUp() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(USER_INITIALIZE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        given(userFacade.register(USER_VO)).willReturn(USER_ID);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.signUp(USER_INITIALIZE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, UserDataModel.getBuilder().withId(USER_ID).build(), LOCATION_HEADER);
    }

    @Test
    public void shouldSignUpWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.signUp(USER_INITIALIZE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldSignUpWithConstraintViolation() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(USER_INITIALIZE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ConstraintViolationException.class).when(userFacade).register(USER_VO);

        // when
        controller.signUp(USER_INITIALIZE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldSignUpWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(USER_INITIALIZE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ServiceException.class).when(userFacade).register(USER_VO);

        // when
        controller.signUp(USER_INITIALIZE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldRevokeToken() throws RequestCouldNotBeFulfilledException {

        // when
        ResponseEntity<Void> result = controller.revokeToken();

        // then
        assertResponse(result, HttpStatus.NO_CONTENT, null);
        verify(userFacade).logout();
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldRevokeTokenWithServiceException() throws RequestCouldNotBeFulfilledException {

        // given
        doThrow(ServiceException.class).when(userFacade).logout();

        // when
        controller.revokeToken();

        // then
        // exception expected
    }

    @Test
    public void shouldDemandPasswordReset() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        // given
        given(loginContextFactory.forPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, httpServletRequest)).willReturn(LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.demandPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, httpServletRequest, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, null);
        verify(userFacade).demandPasswordReset(LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET);
    }

    @Test
    public void shouldDemandPasswordResetWithValidationError() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.demandPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, httpServletRequest, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDemandPasswordResetWithUsernameNotFoundException() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        // given
        given(loginContextFactory.forPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, httpServletRequest)).willReturn(LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET);
        doThrow(UsernameNotFoundException.class).when(userFacade).demandPasswordReset(LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET);

        // when
        controller.demandPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, httpServletRequest, bindingResult);

        // then
        // exception expected
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldDemandPasswordResetWithServiceException() throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        // given
        given(loginContextFactory.forPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, httpServletRequest)).willReturn(LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET);
        doThrow(ServiceException.class).when(userFacade).demandPasswordReset(LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET);

        // when
        controller.demandPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, httpServletRequest, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldConfirmPasswordReset() throws RequestCouldNotBeFulfilledException, EntityNotFoundException {

        // given
        UserPasswordRequestModel userPasswordRequestModel = prepareUserPasswordRequestModel();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.confirmPasswordReset(userPasswordRequestModel, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, null);
        verify(userFacade).confirmPasswordReset(userPasswordRequestModel.getPassword());
    }

    @Test
    public void shouldConfirmPasswordResetWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();
        UserPasswordRequestModel userPasswordRequestModel = prepareUserPasswordRequestModel();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.confirmPasswordReset(userPasswordRequestModel, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldConfirmPasswordResetWithServiceError() throws RequestCouldNotBeFulfilledException, EntityNotFoundException {

        // given
        UserPasswordRequestModel userPasswordRequestModel = prepareUserPasswordRequestModel();
        doThrow(ServiceException.class).when(userFacade).confirmPasswordReset(userPasswordRequestModel.getPassword());

        // when
        controller.confirmPasswordReset(userPasswordRequestModel, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldRenewToken() throws RequestCouldNotBeFulfilledException {

        // given
        given(loginContextFactory.forRenewal(httpServletRequest)).willReturn(LOGIN_CONTEXT_VO_FOR_RENEWAL);
        given(userFacade.extendSession(LOGIN_CONTEXT_VO_FOR_RENEWAL)).willReturn(TOKEN);

        // when
        ResponseEntity<LoginResponseDataModel> result = controller.renewToken(httpServletRequest);

        // then
        assertResponse(result, HttpStatus.CREATED, LOGIN_RESPONSE_DATA_MODEL_WITH_SUCCESS);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldRenewTokenWithServiceException() throws RequestCouldNotBeFulfilledException {

        // given
        given(loginContextFactory.forRenewal(httpServletRequest)).willReturn(LOGIN_CONTEXT_VO_FOR_RENEWAL);
        doThrow(ServiceException.class).when(userFacade).extendSession(LOGIN_CONTEXT_VO_FOR_RENEWAL);

        // when
        controller.renewToken(httpServletRequest);

        // then
        // exception expected
    }

    private UserPasswordRequestModel prepareUserPasswordRequestModel() {

        UserPasswordRequestModel userPasswordRequestModel = new UserPasswordRequestModel();
        userPasswordRequestModel.setPassword(PASSWORD);
        userPasswordRequestModel.setPasswordConfirmation(PASSWORD);

        return userPasswordRequestModel;
    }

    private PasswordChangeRequestModel preparePasswordChangeRequestModel() {

        PasswordChangeRequestModel passwordChangeRequestModel = new PasswordChangeRequestModel();
        passwordChangeRequestModel.setCurrentPassword(PASSWORD);
        passwordChangeRequestModel.setPassword(PASSWORD);
        passwordChangeRequestModel.setPasswordConfirmation(PASSWORD);

        return passwordChangeRequestModel;
    }

    private UpdateRoleRequestModel prepareUpdateRoleRequestModel() {

        UpdateRoleRequestModel updateRoleRequestModel = new UpdateRoleRequestModel();
        updateRoleRequestModel.setRole("ADMIN");

        return updateRoleRequestModel;
    }
}