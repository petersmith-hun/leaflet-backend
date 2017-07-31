package hu.psprog.leaflet.service.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Permits operation for admin and editor users.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated() && hasAnyAuthority('ADMIN', 'EDITOR') ")
public @interface PermitEditorOrAdmin {
}
