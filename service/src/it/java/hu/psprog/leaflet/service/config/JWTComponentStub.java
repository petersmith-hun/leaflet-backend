package hu.psprog.leaflet.service.config;

import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.exception.InvalidAuthorizationHeaderException;
import hu.psprog.leaflet.security.jwt.exception.InvalidJWTTokenException;
import hu.psprog.leaflet.security.jwt.model.JWTAuthenticationAnswerModel;
import hu.psprog.leaflet.security.jwt.model.JWTPayload;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Peter Smith
 */
public class JWTComponentStub implements JWTComponent {

    @Override
    public JWTAuthenticationAnswerModel generateToken(UserDetails userDetails) {
        return null;
    }

    @Override
    public JWTPayload decode(String s) throws InvalidJWTTokenException {
        return null;
    }

    @Override
    public String extractToken(HttpServletRequest httpServletRequest) throws InvalidAuthorizationHeaderException {
        return null;
    }
}
