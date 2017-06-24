package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public String getToken() {
        return token;
    }

    public AuthenticationResult getAuthenticationResult() {
        return authenticationResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AuthResponseVO)) return false;

        AuthResponseVO that = (AuthResponseVO) o;

        return new EqualsBuilder()
                .append(token, that.token)
                .append(authenticationResult, that.authenticationResult)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(token)
                .append(authenticationResult)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .append("authenticationResult", authenticationResult)
                .toString();
    }

    public static AuthResponseVOBuilder getBuilder() {
        return new AuthResponseVOBuilder();
    }

    /**
     * Builder for {@link AuthResponseVO}.
     */
    public static final class AuthResponseVOBuilder {
        private String token;
        private AuthenticationResult authenticationResult;

        private AuthResponseVOBuilder() {
        }

        public AuthResponseVOBuilder withToken(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseVOBuilder withAuthenticationResult(AuthenticationResult authenticationResult) {
            this.authenticationResult = authenticationResult;
            return this;
        }

        public AuthResponseVO build() {
            AuthResponseVO authResponseVO = new AuthResponseVO();
            authResponseVO.authenticationResult = this.authenticationResult;
            authResponseVO.token = this.token;
            return authResponseVO;
        }
    }
}
