package hu.psprog.leaflet.acceptance.suits;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuit;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.acceptance.mock.MockNotificationService;
import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ForbiddenOperationException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import junitparams.JUnitParamsRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel.AuthenticationResult.AUTH_SUCCESS;
import static hu.psprog.leaflet.security.jwt.model.Role.RECLAIM;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

/**
 * Acceptance tests for {@code /users} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuit
public class UsersControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final String HEADER_PARAMETER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_SCHEMA = "Bearer {0}";

    private static final String TEST_ADMIN_EMAIL = "test-admin@ac-leaflet.local";
    private static final String TEST_USER_1_EMAIL = "test-user-1@ac-leaflet.local";
    private static final String TEST_EDITOR_2_EMAIL = "test-editor-2@ac-leaflet.local";
    private static final String TEST_USER_3_EMAIL = "test-user-3@ac-leaflet.local";
    private static final String TEST_EDITOR_4_EMAIL = "test-editor-4@ac-leaflet.local";
    private static final String TEST_EDITOR_5_EMAIL = "test-editor-5@ac-leaflet.local";
    private static final String TEST_USER_6_EMAIL = "test-user-6@ac-leaflet.local";
    private static final String TEST_USER_7_EMAIL = "test-user-7@ac-leaflet.local";
    private static final String TEST_EDITOR_8_EMAIL = "test-editor-8@ac-leaflet.local";
    private static final String NON_EXISTING_USER_EMAIL = "non-existing-user@ac-leaflet.local";

    private static final long ADMIN_USER_ID = 1L;
    private static final long TEST_USER_1_ID = 2L;
    private static final long TEST_EDITOR_5_ID = 6L;
    private static final long TEST_USER_7_ID = 8L;

    private static final String TEST_PASSWORD = "testpw01";
    private static final String UPDATED_PASSWORD = "new-pw";
    private static final String ADMIN_USER_NAME = "Administrator";
    private static final String CONTROL_USER_CREATE = "user-create";
    private static final String CONTROL_USER_REGISTER = "user-register";

    @Autowired
    private UserBridgeService userBridgeService;

    @Autowired
    private MockNotificationService notificationService;

    @Autowired
    private JWTComponent jwtComponent;

    @After
    public void tearDown() {
        Mockito.reset(requestAuthentication);
    }

    @Test
    public void shouldGetAllUsers() throws CommunicationFailureException {

        // when
        UserListDataModel result = userBridgeService.getAllUsers();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getUsers().size(), equalTo(9));
    }

    @Test
    public void shouldGetUserByID() throws CommunicationFailureException {

        // when
        UserDataModel result = userBridgeService.getUserByID(ADMIN_USER_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getUsername(), equalTo(ADMIN_USER_NAME));
    }

    @Test
    @ResetDatabase
    public void shouldUpdateRole() throws CommunicationFailureException {

        // given
        Long userID = 2L;
        UpdateRoleRequestModel updateRoleRequestModel = prepareUpdateRoleRequestModel();

        // when
        userBridgeService.updateRole(userID, updateRoleRequestModel);

        // then
        assertThat(userBridgeService.getUserByID(userID).getRole(), equalTo(Role.EDITOR.name()));
    }

    @Test(expected = ForbiddenOperationException.class)
    public void shouldNotUpdateSelfRole() throws CommunicationFailureException {

        // given
        login(TEST_USER_1_EMAIL, TEST_PASSWORD);
        UpdateRoleRequestModel updateRoleRequestModel = prepareUpdateRoleRequestModel();

        // when
        userBridgeService.updateRole(TEST_USER_1_ID, updateRoleRequestModel);

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldUpdateProfile() throws CommunicationFailureException {

        // given
        UpdateProfileRequestModel updateProfileRequestModel = prepareUpdateProfileRequestModel();

        // when
        userBridgeService.updateProfile(ADMIN_USER_ID, updateProfileRequestModel);

        // then
        ExtendedUserDataModel userData = userBridgeService.getUserByID(ADMIN_USER_ID);
        assertThat(userData.getUsername(), equalTo(updateProfileRequestModel.getUsername()));
        assertThat(userData.getEmail(), equalTo(updateProfileRequestModel.getEmail()));
        assertThat(userData.getLocale().toLowerCase(), equalTo(updateProfileRequestModel.getDefaultLocale().getLanguage()));
    }

    @Test(expected = ForbiddenOperationException.class)
    public void shouldFailUpdatingDifferentUserProfile() throws CommunicationFailureException {

        // given
        UpdateProfileRequestModel updateProfileRequestModel = prepareUpdateProfileRequestModel();

        // when
        userBridgeService.updateProfile(TEST_USER_1_ID, updateProfileRequestModel);

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldUpdatePassword() throws CommunicationFailureException {

        // given
        UserPasswordRequestModel userPasswordRequestModel = prepareUserPasswordRequestModel();

        // when
        userBridgeService.updatePassword(ADMIN_USER_ID, userPasswordRequestModel);

        // then
        LoginResponseDataModel loginResponse = userBridgeService.claimToken(prepareLoginRequestModel(TEST_ADMIN_EMAIL, UPDATED_PASSWORD));
        assertThat(loginResponse.getStatus(), equalTo(AUTH_SUCCESS));
    }

    @Test(expected = ForbiddenOperationException.class)
    public void shouldFailUpdatingDifferentUserPassword() throws CommunicationFailureException {

        // given
        UserPasswordRequestModel userPasswordRequestModel = prepareUserPasswordRequestModel();

        // when
        userBridgeService.updatePassword(TEST_USER_1_ID, userPasswordRequestModel);

        // then
        // exception expected
    }

    @Test
    public void shouldClaimTokenWithExistingUser() throws CommunicationFailureException {

        // given
        LoginRequestModel loginRequestModel = prepareLoginRequestModel(TEST_EDITOR_2_EMAIL, TEST_PASSWORD);

        // when
        LoginResponseDataModel result = userBridgeService.claimToken(loginRequestModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(AUTH_SUCCESS));
        assertThat(result.getToken(), notNullValue());
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void shouldClaimTokenWithNonExistingUser() throws CommunicationFailureException {

        // given
        LoginRequestModel loginRequestModel = prepareLoginRequestModel(NON_EXISTING_USER_EMAIL, TEST_PASSWORD);

        // when
        userBridgeService.claimToken(loginRequestModel);

        // then
        // exception expected
    }

    @Test
    public void shouldStartPasswordResetProcess() throws CommunicationFailureException {

        // given
        PasswordResetDemandRequestModel passwordResetDemandRequestModel = new PasswordResetDemandRequestModel();
        passwordResetDemandRequestModel.setEmail(TEST_USER_3_EMAIL);

        // when
        userBridgeService.demandPasswordReset(passwordResetDemandRequestModel);

        // then
        assertThat(notificationService.getPasswordResetRequest(), notNullValue());
        assertThat(notificationService.getPasswordResetRequest().getToken(), notNullValue());
        assertThat(jwtComponent.decode(notificationService.getPasswordResetRequest().getToken()).getRole(), equalTo(RECLAIM));
    }

    @Test
    @ResetDatabase
    public void shouldConfirmPasswordReset() throws CommunicationFailureException {

        // given
        prepareMockedRequestAuthentication(preparePasswordResetConfirmation(TEST_EDITOR_4_EMAIL));
        UserPasswordRequestModel userPasswordRequestModel = prepareUserPasswordRequestModel();

        // when
        userBridgeService.confirmPasswordReset(userPasswordRequestModel);

        // then
        LoginResponseDataModel loginResponse = userBridgeService.claimToken(prepareLoginRequestModel(TEST_EDITOR_4_EMAIL, UPDATED_PASSWORD));
        assertThat(loginResponse.getStatus(), equalTo(AUTH_SUCCESS));
        assertThat(notificationService.getPasswordResetSuccess().getParticipant(), equalTo(TEST_EDITOR_4_EMAIL));
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void shouldRevokeToken() throws CommunicationFailureException {

        // given
        login(TEST_EDITOR_5_EMAIL, TEST_PASSWORD);
        try {
            UserDataModel userData = userBridgeService.getUserByID(TEST_EDITOR_5_ID);
            assertThat(userData.getId(), equalTo(TEST_EDITOR_5_ID));
        } catch (Exception e) {
            // this call should have been successful
            fail("User should be able to query their own user data.");
        }

        // when
        userBridgeService.revokeToken();

        // then
        // this call should fail and cause exception
        userBridgeService.getUserByID(TEST_EDITOR_5_ID);
    }

    @Test
    public void shouldRenewSession() throws CommunicationFailureException, InterruptedException {

        // given
        login(TEST_USER_6_EMAIL, TEST_PASSWORD);
        Thread.sleep(1200); // without this, test is so fast, it fails

        // when
        LoginResponseDataModel result = userBridgeService.renewToken();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(AUTH_SUCCESS));
    }

    @Test(expected = ResourceNotFoundException.class)
    @ResetDatabase
    public void shouldDeleteSelfUser() throws CommunicationFailureException {

        // given
        login(TEST_USER_7_EMAIL, TEST_PASSWORD);

        // when
        userBridgeService.deleteUser(TEST_USER_7_ID);

        // then
        // this call should fail and cause exception
        userBridgeService.getUserByID(TEST_USER_7_ID);
    }

    @Test(expected = ForbiddenOperationException.class)
    public void shouldNotDeleteDifferentUser() throws CommunicationFailureException {

        // when
        userBridgeService.deleteUser(TEST_USER_7_ID);

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldCreateUser() throws CommunicationFailureException {

        // given
        UserCreateRequestModel userCreateRequestModel = getControl(CONTROL_USER_CREATE, UserCreateRequestModel.class);

        // when
        ExtendedUserDataModel result = userBridgeService.createUser(userCreateRequestModel);

        // then
        assertCreatedUser(userCreateRequestModel, result.getId());
    }

    @Test(expected = ForbiddenOperationException.class)
    public void shouldFailCreatingUserIfNonAdminUserCalls() throws CommunicationFailureException {

        // given
        login(TEST_EDITOR_8_EMAIL, TEST_PASSWORD);
        UserCreateRequestModel userCreateRequestModel = getControl(CONTROL_USER_CREATE, UserCreateRequestModel.class);

        // when
        userBridgeService.createUser(userCreateRequestModel);

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldRegister() throws CommunicationFailureException {

        // given
        UserInitializeRequestModel userInitializeRequestModel = getControl(CONTROL_USER_REGISTER, UserInitializeRequestModel.class);

        // when
        ExtendedUserDataModel result = userBridgeService.signUp(userInitializeRequestModel);

        // then
        assertCreatedUser(userInitializeRequestModel, result.getId());
    }

    private void assertCreatedUser(UserInitializeRequestModel userInitializeRequestModel, long createdUserID) throws CommunicationFailureException {

        ExtendedUserDataModel userData = userBridgeService.getUserByID(createdUserID);
        assertThat(userData.getEmail(), equalTo(userInitializeRequestModel.getEmail()));
        assertThat(userData.getUsername(), equalTo(userInitializeRequestModel.getUsername()));
        assertThat(userData.getLocale().toLowerCase(), equalTo(userInitializeRequestModel.getDefaultLocale().getLanguage()));

        if (userInitializeRequestModel instanceof UserCreateRequestModel) {
            assertThat(userData.getRole(), equalTo(((UserCreateRequestModel) userInitializeRequestModel).getRole()));
        } else {
            assertThat(userData.getRole(), equalTo(Role.USER.name()));
        }
    }

    private UpdateProfileRequestModel prepareUpdateProfileRequestModel() {

        UpdateProfileRequestModel updateProfileRequestModel = new UpdateProfileRequestModel();
        updateProfileRequestModel.setUsername("Modified Username");
        updateProfileRequestModel.setEmail("modified-email@ac-leaflet.local");
        updateProfileRequestModel.setDefaultLocale(Locale.CANADA);

        return updateProfileRequestModel;
    }

    private UpdateRoleRequestModel prepareUpdateRoleRequestModel() {

        UpdateRoleRequestModel updateRoleRequestModel = new UpdateRoleRequestModel();
        updateRoleRequestModel.setRole(Role.EDITOR.name());

        return updateRoleRequestModel;
    }

    private void login(String email, String password) throws CommunicationFailureException {
        LoginResponseDataModel loginResponse = userBridgeService.claimToken(prepareLoginRequestModel(email, password));
        prepareMockedRequestAuthentication(loginResponse.getToken());
    }

    private UserPasswordRequestModel prepareUserPasswordRequestModel() {

        UserPasswordRequestModel userPasswordRequestModel = new UserPasswordRequestModel();
        userPasswordRequestModel.setPassword(UPDATED_PASSWORD);
        userPasswordRequestModel.setPasswordConfirmation(UPDATED_PASSWORD);

        return userPasswordRequestModel;
    }

    private void prepareMockedRequestAuthentication(String token) {

        Map<String, String> authenticationHeader = new HashMap<>();
        authenticationHeader.put(HEADER_PARAMETER_AUTHORIZATION, MessageFormat.format(AUTHORIZATION_SCHEMA, token));
        given(requestAuthentication.getAuthenticationHeader()).willReturn(authenticationHeader);
    }

    private String preparePasswordResetConfirmation(String email) throws CommunicationFailureException {

        PasswordResetDemandRequestModel passwordResetDemandRequestModel = new PasswordResetDemandRequestModel();
        passwordResetDemandRequestModel.setEmail(email);
        userBridgeService.demandPasswordReset(passwordResetDemandRequestModel);

        return notificationService.getPasswordResetRequest().getToken();
    }

    private LoginRequestModel prepareLoginRequestModel(String email, String password) {

        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(email);
        loginRequestModel.setPassword(password);

        return loginRequestModel;
    }
}
