package hu.psprog.leaflet.service.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Security expressions for scope-based authorization.
 *
 * @author Peter Smith
 */
public interface PermitScope {

    /**
     * Scope based security expressions for read operations.
     */
    interface Read {

        /**
         * Permits standard users to read their own account data or elevated users to read other users' account data.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_read:users:own') && @ownershipEvaluator.isSelf(authentication, #id) || hasAuthority('SCOPE_read:users'))")
        @interface OwnUserOrElevated {
        }

        /**
         * Permits standard users to read their own comments or elevated users to read other users' comments.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_read:comments:own') && @ownershipEvaluator.isOwnComment(authentication, #id) || hasAuthority('SCOPE_read:comments'))")
        @interface OwnCommentsOrElevated {
        }

        /**
         * Permits standard users to read their own list of comments or elevated users to read other users' list of comments.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_read:comments:own') && @ownershipEvaluator.isSelf(authentication, #userVO) || hasAuthority('SCOPE_read:comments'))")
        @interface OwnCommentsListOrElevated {
        }

        /**
         * Permits read access for any categories.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_read:categories')")
        @interface Categories {
        }

        /**
         * Permits read access for any comments.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_read:comments')")
        @interface Comments {
        }

        /**
         * Permits read access for any documents.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_read:documents')")
        @interface Documents {
        }

        /**
         * Permits read access for any entries.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_read:entries')")
        @interface Entries {
        }

        /**
         * Permits read access for any tags.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_read:tags')")
        @interface Tags {
        }

        /**
         * Permits read access for any user data.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_read:users')")
        @interface Users {
        }

        /**
         * Permits read access for any administrative operation.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_read:admin')")
        @interface Admin {
        }
    }

    /**
     * Scope based security expressions for write operations.
     */
    interface Write {

        /**
         * Permits standard users to modify their own account data.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:users:own') && @ownershipEvaluator.isSelf(authentication, #id)")
        @interface OwnUser {
        }

        /**
         * Permits standard users to create or modify their own comments or elevated users to create or modify other users' comments.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_write:comments:own') && @ownershipEvaluator.isOwnComment(authentication, #id) || hasAuthority('SCOPE_write:comments'))")
        @interface OwnCommentOrElevated {
        }

        /**
         * Permits standard users to create comments.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:comments:own')")
        @interface CreateComment {
        }

        /**
         * Permits standard users to create or modify their own comments or elevated users to create or modify other users' comments (by entity instead of ID).
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_write:comments:own') && @ownershipEvaluator.isOwnComment(authentication, #entity) || hasAuthority('SCOPE_write:comments'))")
        @interface OwnCommentByEntityOrElevated {
        }

        /**
         * Permits editor users to create or modify their own entries or admin users to create or modify other users' entries.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_write:entries') && @ownershipEvaluator.isOwnEntry(authentication, #id) || hasAuthority('SCOPE_write:admin'))")
        @interface OwnEntryOrElevated {
        }

        /**
         * Permits (elevated) users to create or modify any categories.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:categories')")
        @interface Categories {
        }

        /**
         * Permits (elevated) users to create or modify any comments.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:comments')")
        @interface Comments {
        }

        /**
         * Permits (elevated) users to create or modify any documents.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:documents')")
        @interface Documents {
        }

        /**
         * Permits (elevated) users to create or modify any entries.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:entries')")
        @interface Entries {
        }

        /**
         * Permits (elevated) users to create or modify any tags.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:tags')")
        @interface Tags {
        }

        /**
         * Permits (elevated) users to create or modify any user accounts.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:users')")
        @interface Users {
        }

        /**
         * Permits write access for any administrative operation.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_write:admin')")
        @interface Admin {
        }
    }

    /**
     * Deny-all authorization for deprecated code paths.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("denyAll()")
    @interface DenyAlways {
    }
}
