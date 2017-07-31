package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.security.jwt.model.Role;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.CURRENT_USER_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for security expressions.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityTestContextConfiguration.class)
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

    private static final CommentVO COMMENT = CommentVO.getBuilder()
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

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_USER)
    public void shouldPermitAdminWithFailureOnUserAuthority() {

        // when
        securityExpressionStub.testPermitAdmin();

        // then
        // expected exception
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_EDITOR)
    public void shouldPermitAdminWithFailureOnEditorAuthority() {

        // when
        securityExpressionStub.testPermitAdmin();

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

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_USER)
    public void shouldPermitEditorOrAdminWithFailureOnUserAuthority() {

        // when
        securityExpressionStub.testPermitEditorOrAdmin();

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

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_EDITOR)
    public void shouldPermitServiceOrAdminWithFailureOnEditorAuthority() {

        // when
        securityExpressionStub.testPermitServiceOrAdmin();

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

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = USERNAME, authorities = AUTHORITY_USER)
    public void shouldPermitServiceOrAdminWithFailureOnUserAuthority() {

        // when
        securityExpressionStub.testPermitServiceOrAdmin();

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

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void shouldPermitAuthenticatedWithUnauthenticatedUser() {

        // when
        securityExpressionStub.testPermitAuthenticated();

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

    @Test(expected = AccessDeniedException.class)
    @WithMockedJWTUser(userID = CURRENT_USER_ID, role = Role.USER)
    public void shouldPermitSelfWithFailure() {

        // when
        securityExpressionStub.testPermitSelfUser(3L);

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

    @Test(expected = AccessDeniedException.class)
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.EDITOR)
    public void shouldPermitSelfEntryWithFailureForOtherEditor() {

        // when
        securityExpressionStub.testPermitSelfEntry(1L);

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

    @Test(expected = AccessDeniedException.class)
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.USER)
    public void shouldPermitSelfCommentWithFailureForDifferentUser() {

        // when
        securityExpressionStub.testPermitSelfComment(1L);

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

    @Test(expected = AccessDeniedException.class)
    @WithMockedJWTUser(userID = OTHER_USER_ID, role = Role.USER)
    public void shouldPermitSelfCommentByEntityWithFailureForDifferentUser() {

        // when
        securityExpressionStub.testPermitSelfCommentByEntity(COMMENT);

        // then
        // expected exception
    }
}
