package hu.psprog.leaflet.service.vo;

import java.io.Serializable;

/**
 * Authentication request value object for JWT based sign-in process.
 *
 * @author Peter Smith
 */
public class AuthRequestVO implements Serializable {

    private String username;
    private String password;

    public AuthRequestVO() {
        // Serializable
    }

    public AuthRequestVO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder {

        private String username;
        private String password;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public AuthRequestVO createAuthRequestVO() {
            return new AuthRequestVO(username, password);
        }
    }
}
