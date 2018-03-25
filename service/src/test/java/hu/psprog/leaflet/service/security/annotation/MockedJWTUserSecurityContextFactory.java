package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.JWTPayload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * Factory to create JWT authenticated based security context mock.
 *
 * @author Peter Smith
 */
public class MockedJWTUserSecurityContextFactory implements WithSecurityContextFactory<WithMockedJWTUser> {

    private static final String USERNAME = "user1234";

    @Override
    public SecurityContext createSecurityContext(WithMockedJWTUser withMockedJWTUser) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = JWTAuthenticationToken.getBuilder()
                .withPayload(prepareJWTPayload(withMockedJWTUser))
                .build();
        authentication.setAuthenticated(true);
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

    private JWTPayload prepareJWTPayload(WithMockedJWTUser withMockedJWTUser) {

        return JWTPayload.getBuilder()
                .withId((int) withMockedJWTUser.userID())
                .withRole(withMockedJWTUser.role())
                .withUsername(USERNAME)
                .build();
    }
}
