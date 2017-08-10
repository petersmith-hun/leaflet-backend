package hu.psprog.leaflet.service.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Permits operations for "reclaim" users.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated() && hasAuthority('RECLAIM') && #email == principal")
public @interface PermitReclaim {
}
