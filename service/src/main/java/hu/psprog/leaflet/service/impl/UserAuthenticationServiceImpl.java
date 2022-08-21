package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import hu.psprog.leaflet.security.jwt.model.JWTAuthenticationAnswerModel;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.UserAuthenticationService;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link UserAuthenticationService}.
 *
 * @author Peter Smith
 */
@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);

    private static final int RECLAIM_TOKEN_EXPIRATION_IN_HOURS = 1;
    private static final String RECLAIM_ROLE = "RECLAIM";
    private static final List<String> ELEVATED_ROLES = Arrays.asList("ADMIN", "EDITOR");
    private static final String USERNAME_JWT_ATTRIBUTE = "usr";

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JWTComponent jwtComponent;
    private SessionStoreService sessionStoreService;
    private NotificationService notificationService;

    @Autowired
    public UserAuthenticationServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JWTComponent jwtComponent,
                                         SessionStoreService sessionStoreService, NotificationService notificationService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtComponent = jwtComponent;
        this.sessionStoreService = sessionStoreService;
        this.notificationService = notificationService;
    }

    @Override
    public void reAuthenticate(String password) {
        ExtendedUserDetails userDetails = retrieveAuthenticatedUserDetails();
        authenticationManager.authenticate(createUsernamePasswordAuthentication(userDetails.getUsername(), password));
    }

    @Override
    public String claimToken(LoginContextVO loginContextVO) {

        authenticationManager.authenticate(createUsernamePasswordAuthentication(loginContextVO.getUsername(), loginContextVO.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginContextVO.getUsername());
        JWTAuthenticationAnswerModel authenticationAnswerModel = jwtComponent.generateToken(userDetails);
        storeToken(loginContextVO, authenticationAnswerModel);

        return authenticationAnswerModel.getToken();
    }

    @Override
    @PermitScope.DenyAlways
    public void revokeToken() {

        Authentication authentication = retrieveAuthentication();
        sessionStoreService.revokeToken((JWTAuthenticationToken) authentication);
    }

    @Override
    @PermitScope.DenyAlways
    public void demandPasswordReset(LoginContextVO loginContextVO) {

        LOGGER.info("Password reset process started for user [{}]", loginContextVO.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginContextVO.getUsername());
        UserDetails reclaimUserDetails = generateReclaimUserDetails(userDetails);
        JWTAuthenticationAnswerModel authenticationAnswerModel = jwtComponent.generateToken(reclaimUserDetails, RECLAIM_TOKEN_EXPIRATION_IN_HOURS);
        storeToken(loginContextVO, authenticationAnswerModel);

        notificationService.passwordResetRequested(PasswordResetRequest.getBuilder()
                .withParticipant(loginContextVO.getUsername())
                .withUsername(((ExtendedUserDetails) reclaimUserDetails).getName())
                .withElevated(isElevatedUser(userDetails))
                .withExpiration(RECLAIM_TOKEN_EXPIRATION_IN_HOURS)
                .withToken(authenticationAnswerModel.getToken())
                .build());
    }

    @Override
    @PermitScope.DenyAlways
    public Long confirmPasswordReset() {

        ExtendedUserDetails userDetails = retrieveAuthenticatedUserDetails();
        notificationService.successfulPasswordReset(PasswordResetSuccess.getBuilder()
                .withParticipant(userDetails.getUsername())
                .withUsername(userDetails.getName())
                .build());
        revokeToken();

        LOGGER.info("Password reset confirmed for user [{}]", userDetails.getUsername());

        return userDetails.getId();
    }

    @Override
    @PermitScope.DenyAlways
    public String extendSession(LoginContextVO loginContextVO) {

        ExtendedUserDetails userDetails = retrieveAuthenticatedUserDetails();
        JWTAuthenticationAnswerModel authenticationAnswerModel = jwtComponent.generateToken(userDetails);
        revokeToken();
        storeToken(loginContextVO, authenticationAnswerModel);
        LOGGER.info("Session has been extended for user [{}]", userDetails.getId());

        return authenticationAnswerModel.getToken();
    }

    private boolean isElevatedUser(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ELEVATED_ROLES::contains);
    }

    private ExtendedUserDetails retrieveAuthenticatedUserDetails() {

        String username = (String) ((JwtAuthenticationToken) retrieveAuthentication())
                .getTokenAttributes()
                .get(USERNAME_JWT_ATTRIBUTE);

        return (ExtendedUserDetails) userDetailsService.loadUserByUsername(username);
    }

    private Authentication createUsernamePasswordAuthentication(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    private Authentication retrieveAuthentication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !(authentication instanceof JwtAuthenticationToken)) {
            throw new IllegalStateException("No JWT authentication token found in security context - retrieving authentication object failed.");
        }

        return authentication;
    }

    private UserDetails generateReclaimUserDetails(UserDetails originalUserDetails) {
        ExtendedUserDetails userDetails = (ExtendedUserDetails) originalUserDetails;
        return ExtendedUserDetails.getBuilder()
                .withID(userDetails.getId())
                .withName(userDetails.getName())
                .withUsername(userDetails.getUsername())
                .withEnabled(userDetails.isEnabled())
                .withAuthorities(AuthorityUtils.createAuthorityList(RECLAIM_ROLE))
                .build();
    }

    private void storeToken(LoginContextVO loginContextVO, JWTAuthenticationAnswerModel authenticationAnswerModel) {
        sessionStoreService.storeToken(ClaimedTokenContext.getBuilder()
                .withToken(authenticationAnswerModel.getToken())
                .withRemoteAddress(loginContextVO.getRemoteAddress())
                .withDeviceID(loginContextVO.getDeviceID())
                .build());
    }
}
