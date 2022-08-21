package hu.psprog.leaflet.web.test;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

/**
 * Factory to create JWT authenticated based security context mock.
 *
 * @author Peter Smith
 */
public class MockedJWTUserSecurityContextFactory implements WithSecurityContextFactory<WithMockedJWTUser> {

    private static final String USERNAME = "user1234";

    @Override
    public SecurityContext createSecurityContext(WithMockedJWTUser withMockedJWTUser) {

        Jwt jwt = prepareJWT(withMockedJWTUser);
        Authentication authentication = new JwtAuthenticationToken(jwt, Collections.emptyList());
        authentication.setAuthenticated(true);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

    private Jwt prepareJWT(WithMockedJWTUser withMockedJWTUser) {

        return Jwt.withTokenValue("token1")
                .claim("uid", withMockedJWTUser.userID())
                .claim("usr", USERNAME)
                .header("typ", "JWT")
                .header("alg", "HS256")
                .build();
    }
}
