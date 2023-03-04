package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ForbiddenOperationException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.client.impl.AccessTokenTestUtility;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.persistence.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Acceptance tests for {@code /users} endpoints.
 *
 * @author Peter Smith
 */
@LeafletAcceptanceSuite
public class UsersControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final String HEADER_PARAMETER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_SCHEMA = "Bearer {0}";

    private static final String TEST_USER_1_EMAIL = "test-user-1@ac-leaflet.local";
    private static final String TEST_USER_7_EMAIL = "test-user-7@ac-leaflet.local";
    private static final String TEST_EDITOR_8_EMAIL = "test-editor-8@ac-leaflet.local";

    private static final long ADMIN_USER_ID = 1L;
    private static final long TEST_USER_1_ID = 2L;
    private static final long TEST_USER_7_ID = 8L;

    private static final String TEST_PASSWORD = "testpw01";
    private static final String UPDATED_PASSWORD = "new-pw";
    private static final String ADMIN_USER_NAME = "Administrator";
    private static final String CONTROL_USER_CREATE = "user-create";

    @Autowired
    private UserBridgeService userBridgeService;

    @Autowired
    private AccessTokenTestUtility accessTokenTestUtility;

    @AfterEach
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

    @Test
    public void shouldNotUpdateSelfRole() {

        // given
        oauthLogin(TEST_USER_1_EMAIL);
        UpdateRoleRequestModel updateRoleRequestModel = prepareUpdateRoleRequestModel();

        // when
        Assertions.assertThrows(ForbiddenOperationException.class, () -> userBridgeService.updateRole(TEST_USER_1_ID, updateRoleRequestModel));

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

    @Test
    public void shouldFailUpdatingDifferentUserProfile() {

        // given
        UpdateProfileRequestModel updateProfileRequestModel = prepareUpdateProfileRequestModel();

        // when
        Assertions.assertThrows(ForbiddenOperationException.class, () -> userBridgeService.updateProfile(TEST_USER_1_ID, updateProfileRequestModel));

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
        // silently succeeding
    }

    @Test
    public void shouldFailUpdatingDifferentUserPassword() {

        // given
        UserPasswordRequestModel userPasswordRequestModel = prepareUserPasswordRequestModel();

        // when
        Assertions.assertThrows(ForbiddenOperationException.class, () -> userBridgeService.updatePassword(TEST_USER_1_ID, userPasswordRequestModel));

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldDeleteSelfUser() throws CommunicationFailureException {

        // given
        oauthLogin(TEST_USER_7_EMAIL);

        // when
        userBridgeService.deleteUser(TEST_USER_7_ID);

        // then
        // this call should fail and cause exception
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userBridgeService.getUserByID(TEST_USER_7_ID));
    }

    @Test
    public void shouldNotDeleteDifferentUser() {

        // when
        Assertions.assertThrows(ForbiddenOperationException.class, () -> userBridgeService.deleteUser(TEST_USER_7_ID));

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

    @Test
    public void shouldFailCreatingUserIfNonAdminUserCalls() {

        // given
        oauthLogin(TEST_EDITOR_8_EMAIL);
        UserCreateRequestModel userCreateRequestModel = getControl(CONTROL_USER_CREATE, UserCreateRequestModel.class);

        // when
        Assertions.assertThrows(ForbiddenOperationException.class, () -> userBridgeService.createUser(userCreateRequestModel));

        // then
        // exception expected
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

    private void oauthLogin(String email) {
        String token = accessTokenTestUtility.generateToken(email);
        prepareMockedRequestAuthentication(token);
    }

    private UserPasswordRequestModel prepareUserPasswordRequestModel() {

        PasswordChangeRequestModel userPasswordRequestModel = new PasswordChangeRequestModel();
        userPasswordRequestModel.setPassword(UPDATED_PASSWORD);
        userPasswordRequestModel.setPasswordConfirmation(UPDATED_PASSWORD);
        userPasswordRequestModel.setCurrentPassword(TEST_PASSWORD);

        return userPasswordRequestModel;
    }

    private void prepareMockedRequestAuthentication(String token) {

        Map<String, String> authenticationHeader = new HashMap<>();
        authenticationHeader.put(HEADER_PARAMETER_AUTHORIZATION, MessageFormat.format(AUTHORIZATION_SCHEMA, token));
        given(requestAuthentication.getAuthenticationHeader()).willReturn(authenticationHeader);
    }
}
