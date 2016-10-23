package hu.psprog.leaflet.service.vo;

import java.io.Serializable;

/**
 * Authentication response model for JWT based sign-in process.
 *
 * @author Peter Smith
 */
public class AuthResponseVO implements Serializable {

    /**
     * Authentication result types.
     */
    public enum AuthenticationResult {
        AUTH_SUCCESS,
        INVALID_CREDENTIALS
    }

    private String token;
    private AuthenticationResult authenticationResult;

    public AuthResponseVO() {
        // Serializable
    }

    public AuthResponseVO(String token, AuthenticationResult authenticationResult) {
        this.token = token;
        this.authenticationResult = authenticationResult;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthenticationResult getAuthenticationResult() {
        return authenticationResult;
    }

    public void setAuthenticationResult(AuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }

    public static class Builder {

        private String token;
        private AuthenticationResult authenticationResult;

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public Builder withAuthenticationResult(AuthenticationResult authenticationResult) {
            this.authenticationResult = authenticationResult;
            return this;
        }

        public AuthResponseVO createAuthResponseVO() {
            return new AuthResponseVO(token, authenticationResult);
        }
    }
}
