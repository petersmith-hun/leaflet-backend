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
}
