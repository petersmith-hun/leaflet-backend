package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.exception.ReAuthenticationFailureException;
import hu.psprog.leaflet.service.vo.LoginContextVO;

import static hu.psprog.leaflet.service.config.ServiceConfiguration.AUTH_OPERATION_DEPRECATED_SINCE;

/**
 * Provides logic for user authentication process.
 *
 * @author Peter Smith
 * @deprecated Functionality of this service has been entirely moved to LAGS.
 */
@Deprecated(since = AUTH_OPERATION_DEPRECATED_SINCE, forRemoval = true)
public interface UserAuthenticationService {

    /**
     * Tries to re-authenticate current user with given password.
     * Throws {@link ReAuthenticationFailureException} on failure.
     *
     * @param password current user's password to authenticate with
     * @deprecated Functionality has been moved to LAGS
     */
    @Deprecated(since = AUTH_OPERATION_DEPRECATED_SINCE, forRemoval = true)
    void reAuthenticate(String password);

    /**
     * Requests JWT token for given user (identified by {@link LoginContextVO}).
     *
     * @param loginContextVO login information
     * @return claim result (token if successful)
     * @deprecated Functionality has been moved to LAGS
     */
    @Deprecated(since = AUTH_OPERATION_DEPRECATED_SINCE, forRemoval = true)
    String claimToken(LoginContextVO loginContextVO);

    /**
     * Revokes currently used authentication token.
     *
     * @deprecated Functionality has been moved to LAGS
     */
    @Deprecated(since = AUTH_OPERATION_DEPRECATED_SINCE, forRemoval = true)
    void revokeToken();

    /**
     * Starts password reset process.
     *
     * @param loginContextVO holds user's email address, device ID and remote address parameters
     * @deprecated Functionality has been moved to LAGS
     */
    @Deprecated(since = AUTH_OPERATION_DEPRECATED_SINCE, forRemoval = true)
    void demandPasswordReset(LoginContextVO loginContextVO);

    /**
     * Confirms password reset for currently authenticated RECLAIM user.
     * No explicit user identifier is required, as principal is retrieved from Security Context.
     *
     * @return ID of the user whose password got reset
     * @deprecated Functionality has been moved to LAGS
     */
    @Deprecated(since = AUTH_OPERATION_DEPRECATED_SINCE, forRemoval = true)
    Long confirmPasswordReset();

    /**
     * Extends user session.
     *
     * @param loginContextVO {@link LoginContextVO} object holding client information - only device ID and remote address is required here
     * @return replacement token on success
     * @deprecated Functionality has been moved to LAGS
     */
    @Deprecated(since = AUTH_OPERATION_DEPRECATED_SINCE, forRemoval = true)
    String extendSession(LoginContextVO loginContextVO);
}
