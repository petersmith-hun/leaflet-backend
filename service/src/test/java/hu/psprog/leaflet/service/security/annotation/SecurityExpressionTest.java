package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.ADMIN_UID;
import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.EDITOR_UID;
import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.OTHER_EDITOR_UID;
import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.OTHER_USER_UID;
import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.USER_UID;
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

    private static final long COMMENT_ID = 8L;
    private static final long ENTRY_ID = 9L;
    private static final UserVO USER = UserVO.getBuilder()
            .withId(USER_UID)
            .build();
    private static final CommentVO COMMENT = CommentVO.getBuilder()
            .withId(COMMENT_ID)
            .withOwner(USER)
            .build();

    @Autowired
    private SecurityExpressionStub securityExpressionStub;

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadOwnUserOrElevatedAllowForSelf() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadOwnUserOrElevated(USER_UID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitScopeReadOwnUserOrElevatedAllowForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadOwnUserOrElevated(USER_UID);

        // then
        assertThat(result, is(true));
    }


    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadOwnUserOrElevatedRejectForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadOwnUserOrElevated(USER_UID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadOwnCommentsOrElevatedAllowForSelf() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadOwnCommentsOrElevated(COMMENT_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadOwnCommentsOrElevatedAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadOwnCommentsOrElevated(COMMENT_ID);

        // then
        assertThat(result, is(true));
    }


    @Test
    @WithMockedJWTUser(userID = OTHER_USER_UID, role = Role.USER)
    public void shouldPermitScopeReadOwnCommentsOrElevatedRejectForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadOwnCommentsOrElevated(COMMENT_ID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadOwnCommentsListOrElevatedAllowForSelf() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadOwnCommentsListOrElevated(USER);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadOwnCommentsListOrElevatedAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadOwnCommentsListOrElevated(USER);

        // then
        assertThat(result, is(true));
    }


    @Test
    @WithMockedJWTUser(userID = OTHER_USER_UID, role = Role.USER)
    public void shouldPermitScopeReadOwnCommentsListOrElevatedRejectForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadOwnCommentsListOrElevated(USER));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadCategoriesAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadCategories();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadCategoriesRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadCategories());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadCommentsAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadComments();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadCommentsRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadComments());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadDocumentsAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadDocuments();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadDocumentsRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadDocuments());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadEntriesAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadEntries();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadEntriesRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadEntries());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadTagsAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadTags();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadTagsRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadTags());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitScopeReadUsersAllowForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadUsers();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadUsersRejectForEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadUsers());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadUsersRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadUsers());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitScopeReadAdminAllowForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitScopeReadAdmin();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeReadAdminRejectForEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadAdmin());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeReadAdminRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeReadAdmin());

        // then
        // exception expected
    }


    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteOwnUserAllowForSelf() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteOwnUser(USER_UID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitScopeWriteOwnUserRejectForAdmin() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteOwnUser(USER_UID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteOwnUserRejectForEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteOwnUser(USER_UID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteOwnUserRejectForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteOwnUser(USER_UID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteOwnCommentsOrElevatedAllowForSelf() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteOwnCommentOrElevated(COMMENT_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteOwnCommentsOrElevatedAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteOwnCommentOrElevated(COMMENT_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteOwnCommentsOrElevatedRejectForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteOwnCommentOrElevated(COMMENT_ID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteOwnCommentByEntityOrElevatedAllowForSelf() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteOwnCommentByEntityOrElevated(COMMENT);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteOwnCommentByEntityOrElevatedAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteOwnCommentByEntityOrElevated(COMMENT);

        // then
        assertThat(result, is(true));
    }


    @Test
    @WithMockedJWTUser(userID = OTHER_USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteOwnCommentByEntityOrElevatedRejectForDifferentUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteOwnCommentByEntityOrElevated(COMMENT));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteOwnEntryOrElevatedAllowForSelf() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteOwnEntryOrElevated(ENTRY_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitScopeWriteOwnEntryOrElevatedAllowForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteOwnEntryOrElevated(ENTRY_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteOwnEntryOrElevatedRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteOwnEntryOrElevated(ENTRY_ID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = OTHER_EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteOwnEntryOrElevatedRejectForOtherEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteOwnEntryOrElevated(ENTRY_ID));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteCategoriesAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteCategories();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteCategoriesRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteCategories());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteCommentsAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteComments();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteCommentsRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteComments());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteDocumentsAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteDocuments();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteDocumentsRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteDocuments());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteEntriesAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteEntries();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteEntriesRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteEntries());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteTagsAllowForModerator() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteTags();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteTagsRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteTags());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitScopeWriteUsersAllowForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteUsers();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteUsersRejectForEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteUsers());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteUsersRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteUsers());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitScopeWriteAdminAllowForAdmin() {

        // when
        boolean result = securityExpressionStub.testPermitScopeWriteAdmin();

        // then
        assertThat(result, is(true));
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitScopeWriteAdminRejectForEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteAdmin());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitScopeWriteAdminRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testPermitScopeWriteAdmin());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = ADMIN_UID, role = Role.ADMIN)
    public void shouldPermitDenyAllRejectForAdmin() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testDenyAlways());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = EDITOR_UID, role = Role.EDITOR)
    public void shouldPermitDenyAllRejectForEditor() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testDenyAlways());

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser(userID = USER_UID, role = Role.USER)
    public void shouldPermitDenyAllRejectForUser() {

        // when
        Assertions.assertThrows(AccessDeniedException.class, () -> securityExpressionStub.testDenyAlways());

        // then
        // exception expected
    }
}
