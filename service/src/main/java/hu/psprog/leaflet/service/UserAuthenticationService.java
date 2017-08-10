package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.vo.LoginContextVO;

/**
 * Provides logic for user authentication process.
 *
 * @author Peter Smith
 */
public interface UserAuthenticationService {

    /**
     * Requests JWT token for given user (identified by {@link LoginContextVO}).
     *
     * @param loginContextVO login information
     * @return claim result (token if successful)
     */
    String claimToken(LoginContextVO loginContextVO);

    /**
     * Revokes currently used authentication token.
     */
    void revokeToken();

    /**
     * Starts password reset process.
     *
     * @param loginContextVO holds user's email address, device ID and remote address parameters
     */
    void demandPasswordReset(LoginContextVO loginContextVO);

    /**
     * Confirms password reset and updates password of the user.
     *
     * @param email email address of the user to request password reset for
     * @param password user's new password
     */
    void confirmPasswordReset(String email, String password);
}
