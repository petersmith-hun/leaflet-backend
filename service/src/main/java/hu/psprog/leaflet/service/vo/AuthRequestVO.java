package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Authentication request value object for JWT based sign-in process.
 *
 * @author Peter Smith
 */
public class AuthRequestVO implements Serializable {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AuthRequestVO)) return false;

        AuthRequestVO that = (AuthRequestVO) o;

        return new EqualsBuilder()
                .append(username, that.username)
                .append(password, that.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(password)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("password", "[hidden]")
                .toString();
    }

    public static AuthRequestVOBuilder getBuilder() {
        return new AuthRequestVOBuilder();
    }

    /**
     * Builder for {@link AuthRequestVO}.
     */
    public static final class AuthRequestVOBuilder {
        private String username;
        private String password;

        private AuthRequestVOBuilder() {
        }

        public AuthRequestVOBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public AuthRequestVOBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public AuthRequestVO build() {
            AuthRequestVO authRequestVO = new AuthRequestVO();
            authRequestVO.password = this.password;
            authRequestVO.username = this.username;
            return authRequestVO;
        }
    }
}
