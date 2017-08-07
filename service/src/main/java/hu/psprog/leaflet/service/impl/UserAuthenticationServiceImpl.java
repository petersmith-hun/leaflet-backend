package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.JWTAuthenticationAnswerModel;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import hu.psprog.leaflet.service.UserAuthenticationService;
import hu.psprog.leaflet.service.security.annotation.PermitAuthenticated;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementation of {@link UserAuthenticationService}.
 *
 * @author Peter Smith
 */
@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JWTComponent jwtComponent;
    private SessionStoreService sessionStoreService;

    @Autowired
    public UserAuthenticationServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                                         JWTComponent jwtComponent, SessionStoreService sessionStoreService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtComponent = jwtComponent;
        this.sessionStoreService = sessionStoreService;
    }

    @Override
    public String claimToken(LoginContextVO loginContextVO) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginContextVO.getUsername(), loginContextVO.getPassword());
        authenticationManager.authenticate(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginContextVO.getUsername());
        JWTAuthenticationAnswerModel authenticationAnswer = jwtComponent.generateToken(userDetails);

        sessionStoreService.storeToken(ClaimedTokenContext.getBuilder()
                .withToken(authenticationAnswer.getToken())
                .withDeviceID(loginContextVO.getDeviceID())
                .withRemoteAddress(loginContextVO.getRemoteAddress())
                .build());

        return authenticationAnswer.getToken();
    }

    @Override
    @PermitAuthenticated
    public void revokeToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !(authentication instanceof JWTAuthenticationToken)) {
            throw new IllegalStateException("No JWT authentication token found in security context - revoke failed.");
        }

        sessionStoreService.revokeToken((JWTAuthenticationToken) authentication);
    }
}
