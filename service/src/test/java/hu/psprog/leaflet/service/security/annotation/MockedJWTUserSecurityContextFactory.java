package hu.psprog.leaflet.service.security.annotation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

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
        List<GrantedAuthority> authorities = RoleToAuthoritiesMapping.getAuthoritiesForRole(withMockedJWTUser.role());
        Authentication authentication = new JwtAuthenticationToken(jwt, authorities);
        authentication.setAuthenticated(true);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

    private Jwt prepareJWT(WithMockedJWTUser withMockedJWTUser) {

        return Jwt.withTokenValue("token1")
                .claim("uid", withMockedJWTUser.userID())
                .claim("usr", USERNAME)
                .claim("rol", withMockedJWTUser.role())
                .header("typ", "JWT")
                .header("alg", "HS256")
                .build();
    }
}
