package hu.psprog.leaflet.service.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Permits operation for any authenticated user.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated() && hasAnyAuthority('ADMIN', 'EDITOR', 'USER')")
public @interface PermitAuthenticated {
}
