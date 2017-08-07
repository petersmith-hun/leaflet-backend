package hu.psprog.leaflet.service.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Permits self-modifying operations.
 *
 * @author Peter Smith
 */
public interface PermitSelf {

    /**
     * Permits user self-modification operations: updating profile, password, etc.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("isAuthenticated() && hasAnyAuthority('ADMIN', 'EDITOR', 'USER') && @ownershipEvaluator.isSelf(authentication, #id)")
    @interface User {
    }

    /**
     * Permits entry modification for entry owners.
     * Administrator can modify other editors' entries as well.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("isAuthenticated() && hasAnyAuthority('ADMIN', 'EDITOR') && @ownershipEvaluator.isOwnEntryOrAdmin(authentication, #id)")
    @interface Entry {
    }

    /**
     * Permits comment modification by owner and "moderators" (editor and admin users).
     */
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("isAuthenticated() && hasAnyAuthority('ADMIN', 'EDITOR', 'USER') && @ownershipEvaluator.isOwnCommentOrModerator(authentication, #id)")
    @interface Comment {
    }

    /**
     * Permits comment modification by owner and "moderators" (editor and admin users).
     */
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("isAuthenticated() && hasAnyAuthority('ADMIN', 'EDITOR', 'USER') && @ownershipEvaluator.isOwnCommentOrModerator(authentication, #entity)")
    @interface CommentByEntity {
    }
}
