package hu.psprog.leaflet.web.test;

import hu.psprog.leaflet.security.jwt.model.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Mocked JWT user.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockedJWTUserSecurityContextFactory.class)
public @interface WithMockedJWTUser {

    long userID();
    Role role();
}
