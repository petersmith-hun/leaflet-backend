package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.security.jwt.model.Role;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.CURRENT_USER_ID;
import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.SECURITY_TEST_PROFILE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for security expressions.
 *
 * @author Peter Smith
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityTestContextConfiguration.class)
@ActiveProfiles(SECURITY_TEST_PROFILE)
@TestExecutionListeners(listeners = {ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class SecurityExpressionTest {

    private static final String USERNAME = "user1234";
    private static final long OTHER_USER_ID = 3L;

    private static final String AUTHORITY_ADMIN = "ADMIN";
    private static final String AUTHORITY_EDITOR = "EDITOR";
    private static final String AUTHORITY_USER = "USER";
    private static final String AUTHORITY_SERVICE = "SERVICE";

    private static final long COMMENT_ID = 8L;
    private static final CommentVO COMMENT = CommentVO.getBuilder()
            .withId(COMMENT_ID)
            .withOwner(UserVO.getBuilder()
                    .withId(CURRENT_USER_ID)
                    .build())
            .build();

    @Autowired
    private SecurityExpressionStub securityExpressionStub;

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_ADMIN)
    public void shouldPermitAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitAdmin();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_USER)
    public void shouldPermitAdminWithFailureOnUserAuthority() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitAdmin());

        // then
        // expected exception
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_EDITOR)
    public void shouldPermitAdminWithFailureOnEditorAuthority() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitAdmin());

        // then
        // expected exception
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_ADMIN)
    public void shouldPermitEditorOrAdminWithAdminAuthority() {

        // when
        boolean result = securityExpressionStub.testPermitEditorOrAdmin();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_EDITOR)
    public void shouldPermitEditorOrAdminWithEditorAuthority() {

        // when
        boolean result = securityExpressionStub.testPermitEditorOrAdmin();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_USER)
    public void shouldPermitEditorOrAdminWithFailureOnUserAuthority() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitEditorOrAdmin());

        // then
        // expected exception
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_ADMIN)
    public void shouldPermitServiceOrAdminWithAdminAuthority() {

        // when
        boolean result = securityExpressionStub.testPermitServiceOrAdmin();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_EDITOR)
    public void shouldPermitServiceOrAdminWithFailureOnEditorAuthority() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitServiceOrAdmin());

        // then
        // expected exception
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_SERVICE)
    public void shouldPermitServiceOrAdminWithServiceAuthority() {

        // when
        boolean result = securityExpressionStub.testPermitServiceOrAdmin();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_USER)
    public void shouldPermitServiceOrAdminWithFailureOnUserAuthority() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitServiceOrAdmin());

        // then
        // expected exception
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_ADMIN)
    public void shouldPermitAuthenticatedWithAdminAuthority() {

        // when
        boolean result = securityExpressionStub.testPermitAuthenticated();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_EDITOR)
    public void shouldPermitAuthenticatedWithEditorAuthority() {

        // when
        boolean result = securityExpressionStub.testPermitAuthenticated();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_USER)
    public void shouldPermitAuthenticatedWithUserAuthority() {

        // when
        boolean result = securityExpressionStub.testPermitAuthenticated();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldPermitAuthenticatedWithUnauthenticatedUser() {

        // when
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> securityExpressionStub.testPermitAuthenticated());

        // then
        // expected exception
    }

    @Test
    @WithMockedJWTUser(userID = CURRENT_USER_ID, role = Role.USER)
    public void shouldPermitSelf() {

        // when
        boolean result = securityExpressionStub.testPermitSelfUser(CURRENT_USER_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = CURRENT_USER_ID, role = Role.USER)
    public void shouldPermitSelfWithFailure() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitSelfUser(3L));

        // then
        // expected exception
    }

    @Test
    @WithMockedJWTUser(userID = CURRENT_USER_ID, role = Role.EDITOR)
    public void shouldPermitSelfEntryForOwnEntry() {

        // when
        boolean result = securityExpressionStub.testPermitSelfEntry(1L);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.ADMIN)
    public void shouldPermitSelfEntryForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitSelfEntry(1L);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.EDITOR)
    public void shouldPermitSelfEntryWithFailureForOtherEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitSelfEntry(1L));

        // then
        // expected exception
    }

    @Test
    @WithMockedJWTUser(userID = CURRENT_USER_ID, role = Role.USER)
    public void shouldPermitSelfComment() {

        // when
        boolean result = securityExpressionStub.testPermitSelfComment(1L);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.EDITOR)
    public void shouldPermitSelfCommentForEditor() {

        // when
        boolean result = securityExpressionStub.testPermitSelfComment(1L);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.ADMIN)
    public void shouldPermitSelfCommentForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitSelfComment(1L);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.USER)
    public void shouldPermitSelfCommentWithFailureForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitSelfComment(1L));

        // then
        // expected exception
    }

    @Test
    @WithMockedJWTUser(userID = CURRENT_USER_ID, role = Role.USER)
    public void shouldPermitSelfCommentByEntity() {

        // when
        boolean result = securityExpressionStub.testPermitSelfCommentByEntity(COMMENT);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.EDITOR)
    public void shouldPermitSelfCommentByEntityForEditor() {

        // when
        boolean result = securityExpressionStub.testPermitSelfCommentByEntity(COMMENT);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.ADMIN)
    public void shouldPermitSelfCommentByEntityForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitSelfCommentByEntity(COMMENT);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.USER)
    public void shouldPermitSelfCommentByEntityWithFailureForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitSelfCommentByEntity(COMMENT));

        // then
        // expected exception
    }
}
