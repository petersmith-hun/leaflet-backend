package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import hu.psprog.leaflet.security.jwt.model.JWTAuthenticationAnswerModel;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.UserAuthenticationService;
import hu.psprog.leaflet.service.security.annotation.PermitAuthenticated;
import hu.psprog.leaflet.service.security.annotation.PermitReclaim;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
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

    private static final int RECLAIM_TOKEN_EXPIRATION_IN_HOURS = 1;
    private static final String RECLAIM_ROLE = "RECLAIM";

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JWTComponent jwtComponent;
    private SessionStoreService sessionStoreService;
    private NotificationService notificationService;
    private UserDAO userDAO;

    @Autowired
    public UserAuthenticationServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JWTComponent jwtComponent,
                                         SessionStoreService sessionStoreService, NotificationService notificationService, UserDAO userDAO) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtComponent = jwtComponent;
        this.sessionStoreService = sessionStoreService;
        this.notificationService = notificationService;
        this.userDAO = userDAO;
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

    @Override
    public void demandPasswordReset(LoginContextVO loginContextVO) {

        UserDetails reclaimUserDetails = generateReclaimUserDetails(userDetailsService.loadUserByUsername(loginContextVO.getUsername()));
        JWTAuthenticationAnswerModel authenticationAnswerModel = jwtComponent.generateToken(reclaimUserDetails, RECLAIM_TOKEN_EXPIRATION_IN_HOURS);
        sessionStoreService.storeToken(ClaimedTokenContext.getBuilder()
                .withToken(authenticationAnswerModel.getToken())
                .withRemoteAddress(loginContextVO.getRemoteAddress())
                .withDeviceID(loginContextVO.getDeviceID())
                .build());

        notificationService.passwordResetRequested(authenticationAnswerModel.getToken());
    }

    @Override
    @PermitReclaim
    public void confirmPasswordReset(@P("email") String email, String password) {

        ExtendedUserDetails userDetails = (ExtendedUserDetails) userDetailsService.loadUserByUsername(email);
        userDAO.updatePassword(userDetails.getId(), password);
        notificationService.successfulPasswordReset();
        revokeToken();
    }

    private UserDetails generateReclaimUserDetails(UserDetails originalUserDetails) {
        ExtendedUserDetails userDetails = (ExtendedUserDetails) originalUserDetails;
        return new ExtendedUserDetails.Builder()
                .withID(userDetails.getId())
                .withName(userDetails.getName())
                .withUsername(userDetails.getUsername())
                .withEnabled(userDetails.isEnabled())
                .withAuthorities(AuthorityUtils.createAuthorityList(RECLAIM_ROLE))
                .build();
    }
}
