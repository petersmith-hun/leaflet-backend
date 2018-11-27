package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.UserAuthenticationService;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ReAuthenticationFailureException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import hu.psprog.leaflet.service.vo.UserVO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class UserFacadeImplTest {

    private static final long USER_ID = 11L;
    private static final UserVO USER_VO = UserVO.wrapMinimumVO(USER_ID);
    private static final String PASSWORD = "test-pw";
    private static final String NEW_PASSWORD = "test-pw-new";
    private static final String ENCODED_PASSWORD = "test-pw-encoded";
    private static final UserVO USER_TO_CREATE = UserVO.getBuilder()
            .withPassword(PASSWORD)
            .build();
    private static final UserVO REBUILT_USER = UserVO.getBuilder()
            .withPassword(ENCODED_PASSWORD)
            .build();
    private static final LoginContextVO LOGIN_CONTEXT = LoginContextVO.getBuilder()
            .withUsername("username")
            .build();

    @Mock
    private UserService userService;

    @Mock
    private UserAuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserFacadeImpl userFacade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetUserList() {

        // when
        userFacade.getUserList();

        // then
        verify(userService).getAll();
    }

    @Test
    public void shouldCreateUser() throws ServiceException {

        // given
        given(passwordEncoder.encode(PASSWORD)).willReturn(ENCODED_PASSWORD);
        given(userService.createOne(REBUILT_USER)).willReturn(USER_ID);

        // when
        userFacade.createUser(USER_TO_CREATE);

        // then
        verify(userService).createOne(REBUILT_USER);
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldGetUserByID() throws ServiceException {

        // when
        userFacade.getUserByID(USER_ID);

        // then
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldDeleteUserByID() throws ServiceException {

        // when
        userFacade.deleteUserByID(USER_ID);

        // then
        verify(userService).deleteByID(USER_ID);
    }

    @Test
    @Parameters(source = UserFacadeTestParameterProvider.class, method = "authority")
    public void shouldChangeAuthority(String authorityName, GrantedAuthority expectedAuthority) throws ServiceException {

        // when
        userFacade.changeAuthority(USER_ID, authorityName);

        // then
        verify(userService).changeAuthority(USER_ID, expectedAuthority);
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldUpdateProfile() throws ServiceException {

        // when
        userFacade.updateUserProfile(USER_ID, USER_VO);

        // then
        verify(userService).updateOne(USER_ID, USER_VO);
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldUpdatePassword() throws ServiceException {

        // given
        given(passwordEncoder.encode(NEW_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userFacade.updateUserPassword(USER_ID, PASSWORD, NEW_PASSWORD);

        // then
        verify(authenticationService).reAuthenticate(PASSWORD);
        verify(userService).changePassword(USER_ID, ENCODED_PASSWORD);
        verify(userService).getOne(USER_ID);
    }

    @Test(expected = ReAuthenticationFailureException.class)
    public void shouldUpdatePasswordWithReAuthenticationException() throws ServiceException {

        // given
        doThrow(BadCredentialsException.class).when(authenticationService).reAuthenticate(PASSWORD);

        // when
        userFacade.updateUserPassword(USER_ID, PASSWORD, NEW_PASSWORD);

        // then
        // exception expected
    }

    @Test
    public void shouldLogin() throws EntityNotFoundException {

        // given
        String token = "token";
        given(authenticationService.claimToken(any(LoginContextVO.class))).willReturn(token);

        // when
        String result = userFacade.login(LOGIN_CONTEXT);

        // then
        assertThat(result, equalTo(token));
        verify(authenticationService).claimToken(LOGIN_CONTEXT);
        verify(userService).updateLastLogin(LOGIN_CONTEXT.getUsername());
    }

    @Test
    public void shouldRegister() throws ServiceException {

        // given
        given(passwordEncoder.encode(PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userFacade.register(USER_TO_CREATE);

        // then
        verify(userService).register(REBUILT_USER);
    }

    @Test
    public void shouldLogout() {

        // when
        userFacade.logout();

        // then
        verify(authenticationService).revokeToken();
    }

    @Test
    public void shouldDemandPasswordReset() {

        // when
        userFacade.demandPasswordReset(LOGIN_CONTEXT);

        // then
        verify(authenticationService).demandPasswordReset(LOGIN_CONTEXT);
    }

    @Test
    public void shouldConfirmPasswordReset() throws EntityNotFoundException {

        // given
        given(authenticationService.confirmPasswordReset()).willReturn(USER_ID);
        given(passwordEncoder.encode(PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userFacade.confirmPasswordReset(PASSWORD);

        // then
        verify(userService).reclaimPassword(USER_ID, ENCODED_PASSWORD);
    }

    @Test
    public void shouldExtendSession() {

        // when
        userFacade.extendSession(LOGIN_CONTEXT);

        // then
        verify(authenticationService).extendSession(LOGIN_CONTEXT);
    }

    public static class UserFacadeTestParameterProvider {

        public static Object[] authority() {
            return new Object[] {
                    new Object[] {"ADMIN", Authority.ADMIN},
                    new Object[] {"EDITOR", Authority.EDITOR},
                    new Object[] {"SERVICE", Authority.SERVICE},
                    new Object[] {"NO_LOGIN", Authority.NO_LOGIN},
                    new Object[] {"USER", Authority.USER}
            };
        }
    }
}