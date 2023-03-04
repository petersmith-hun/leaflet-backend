package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.UserFacade;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UsersController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UsersControllerTest extends AbstractControllerBaseTest {

    private static final List<UserVO> USER_VO_LIST = Collections.singletonList(USER_VO);
    private static final long USER_ID = 1L;
    private static final String LOCATION_HEADER = "/users/" + USER_ID;

    @Mock
    private UserFacade userFacade;

    @InjectMocks
    private UsersController controller;

    @BeforeEach
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

    @Test
    public void shouldCreateUserWithConstraintViolation() throws ServiceException {

        // given
        given(conversionService.convert(USER_CREATE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ConstraintViolationException.class).when(userFacade).createUser(USER_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.createUser(USER_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateUserWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(USER_CREATE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ServiceException.class).when(userFacade).createUser(USER_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.createUser(USER_CREATE_REQUEST_MODEL, bindingResult));

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

    @Test
    public void shouldGetUserByIDWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(userFacade).getUserByID(USER_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.getUserByID(USER_ID));

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

    @Test
    public void shouldDeleteUserWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(userFacade).deleteUserByID(USER_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.deleteUser(USER_ID));

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

    @Test
    public void shouldUpdateRoleWithServiceException() throws ServiceException {

        // given
        UpdateRoleRequestModel updateRoleRequestModel = prepareUpdateRoleRequestModel();
        doThrow(ServiceException.class).when(userFacade).changeAuthority(USER_ID, updateRoleRequestModel.getRole());

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.updateRole(USER_ID, updateRoleRequestModel, bindingResult));

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

    @Test
    public void shouldUpdateProfileWithConstraintViolation() throws ServiceException {

        // given
        given(conversionService.convert(UPDATE_PROFILE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ConstraintViolationException.class).when(userFacade).updateUserProfile(USER_ID, USER_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateProfileWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(UPDATE_PROFILE_REQUEST_MODEL, UserVO.class)).willReturn(USER_VO);
        doThrow(ServiceException.class).when(userFacade).updateUserProfile(USER_ID, USER_VO);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL, bindingResult));

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

    @Test
    public void shouldUpdatePasswordWithServiceException() throws ServiceException {

        // given
        PasswordChangeRequestModel passwordChangeRequestModel = preparePasswordChangeRequestModel();
        doThrow(ServiceException.class).when(userFacade).updateUserPassword(USER_ID, passwordChangeRequestModel.getCurrentPassword(), passwordChangeRequestModel.getPassword());

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.updatePassword(USER_ID, passwordChangeRequestModel, bindingResult));

        // then
        // exception expected
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