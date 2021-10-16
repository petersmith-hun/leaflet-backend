package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import hu.psprog.leaflet.security.jwt.model.JWTAuthenticationAnswerModel;
import hu.psprog.leaflet.security.jwt.model.Role;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.security.annotation.WithMockedJWTUser;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserAuthenticationServiceImpl}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(SpringExtension.class),
        @ExtendWith(MockitoExtension.class)
})
@TestExecutionListeners(listeners = {
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class UserAuthenticationServiceImplTest {

    private static final String TOKEN = "token";
    private static final String PASSWORD = "new-pw";
    private static final String EMAIL_ADDRESS = "test@local.dev";
    private static final String NAME = "name";
    private static final long USER_ID = 1L;
    private static final boolean ENABLED = true;
    private static final int EXPIRATION = 1;
    private static final LoginContextVO LOGIN_CONTEXT_VO = LoginContextVO.getBuilder()
            .withUsername(EMAIL_ADDRESS)
            .withPassword(PASSWORD)
            .withDeviceID(UUID.randomUUID())
            .withRemoteAddress("localhost")
            .build();
    private static final ExtendedUserDetails EXTENDED_USER_DETAILS = ExtendedUserDetails.getBuilder()
            .withName(NAME)
            .withID(USER_ID)
            .withUsername(EMAIL_ADDRESS)
            .withAuthorities(Collections.singletonList(Authority.ADMIN))
            .withEnabled(ENABLED)
            .build();
    private static final ExtendedUserDetails RECLAIM_USER_DETAILS = ExtendedUserDetails.getBuilder()
            .withName(NAME)
            .withID(USER_ID)
            .withUsername(EMAIL_ADDRESS)
            .withAuthorities(AuthorityUtils.createAuthorityList("RECLAIM"))
            .withEnabled(ENABLED)
            .build();
    private static final UsernamePasswordAuthenticationToken USERNAME_PASSWORD_AUTHENTICATION_TOKEN
            = new UsernamePasswordAuthenticationToken(EXTENDED_USER_DETAILS.getUsername(), PASSWORD);
    private static final JWTAuthenticationAnswerModel JWT_AUTHENTICATION_ANSWER_MODEL = JWTAuthenticationAnswerModel.getBuilder()
            .withToken(TOKEN)
            .build();

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JWTComponent jwtComponent;

    @Mock
    private SessionStoreService sessionStoreService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserAuthenticationServiceImpl userAuthenticationService;

    @Test
    @WithMockedJWTUser(userID = USER_ID, role = Role.ADMIN)
    public void shouldReAuthenticate() {

        // given
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(EXTENDED_USER_DETAILS);

        // when
        userAuthenticationService.reAuthenticate(PASSWORD);

        // then
        verify(authenticationManager).authenticate(USERNAME_PASSWORD_AUTHENTICATION_TOKEN);
    }

    @Test
    @WithMockUser
    public void shouldReAuthenticateThrowExceptionIfNonJWTAuthenticated() {

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> userAuthenticationService.reAuthenticate(PASSWORD));

        // then
        // exception expected
    }

    @Test
    public void shouldReAuthenticateThrowExceptionIfNonAuthenticated() {

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> userAuthenticationService.reAuthenticate(PASSWORD));

        // then
        // exception expected
    }

    @Test
    public void shouldClaimToken() {

        // given
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(EXTENDED_USER_DETAILS);
        given(jwtComponent.generateToken(any(UserDetails.class))).willReturn(JWT_AUTHENTICATION_ANSWER_MODEL);

        // when
        userAuthenticationService.claimToken(LOGIN_CONTEXT_VO);

        // then
        verify(authenticationManager).authenticate(USERNAME_PASSWORD_AUTHENTICATION_TOKEN);
        verify(userDetailsService).loadUserByUsername(EMAIL_ADDRESS);
        verify(jwtComponent).generateToken(EXTENDED_USER_DETAILS);
        verifySessionStoreCall();
    }

    @Test
    @WithMockedJWTUser(userID = USER_ID, role = Role.ADMIN)
    public void shouldRevokeToken() {

        // when
        userAuthenticationService.revokeToken();

        // then
        verify(sessionStoreService).revokeToken(any(JWTAuthenticationToken.class));
    }

    @Test
    public void shouldDemandPasswordReset() {

        // given
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(EXTENDED_USER_DETAILS);
        given(jwtComponent.generateToken(any(UserDetails.class), eq(1))).willReturn(JWT_AUTHENTICATION_ANSWER_MODEL);

        // when
        userAuthenticationService.demandPasswordReset(LOGIN_CONTEXT_VO);

        // then
        verify(userDetailsService).loadUserByUsername(EMAIL_ADDRESS);
        verify(jwtComponent).generateToken(RECLAIM_USER_DETAILS, EXPIRATION);
        verifySessionStoreCall();
        verifyNotificationServiceCallForPasswordResetDemand();
    }

    @Test
    @WithMockedJWTUser(userID = 1L, role = Role.ADMIN)
    public void shouldConfirmPasswordReset() {

        // given
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(EXTENDED_USER_DETAILS);

        // when
        Long result = userAuthenticationService.confirmPasswordReset();

        // then
        assertThat(result, equalTo(EXTENDED_USER_DETAILS.getId()));
        verify(sessionStoreService).revokeToken(any(JWTAuthenticationToken.class));
        verifyNotificationServiceCallForPasswordResetConfirmation();
    }

    @Test
    @WithMockedJWTUser(userID = 1L, role = Role.ADMIN)
    public void shouldExtendSession() {

        // given
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(EXTENDED_USER_DETAILS);
        given(jwtComponent.generateToken(any(UserDetails.class))).willReturn(JWT_AUTHENTICATION_ANSWER_MODEL);

        // when
        String result = userAuthenticationService.extendSession(LOGIN_CONTEXT_VO);

        // then
        assertThat(result, equalTo(TOKEN));
        verify(sessionStoreService).revokeToken(any(JWTAuthenticationToken.class));
        verifySessionStoreCall();
    }

    private void verifySessionStoreCall() {
        verify(sessionStoreService).storeToken(ClaimedTokenContext.getBuilder()
                .withToken(TOKEN)
                .withDeviceID(LOGIN_CONTEXT_VO.getDeviceID())
                .withRemoteAddress(LOGIN_CONTEXT_VO.getRemoteAddress())
                .build());
    }

    private void verifyNotificationServiceCallForPasswordResetDemand() {
        verify(notificationService).passwordResetRequested(PasswordResetRequest.getBuilder()
                .withParticipant(LOGIN_CONTEXT_VO.getUsername())
                .withUsername(RECLAIM_USER_DETAILS.getName())
                .withElevated(true)
                .withExpiration(EXPIRATION)
                .withToken(TOKEN)
                .build());
    }

    private void verifyNotificationServiceCallForPasswordResetConfirmation() {
        verify(notificationService).successfulPasswordReset(PasswordResetSuccess.getBuilder()
                .withParticipant(EXTENDED_USER_DETAILS.getUsername())
                .withUsername(EXTENDED_USER_DETAILS.getName())
                .build());
    }
}