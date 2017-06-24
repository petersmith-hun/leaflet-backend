package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * VO for {@link User} entity.
 *
 * @author Peter Smith
 */
public class UserVO extends SelfStatusAwareIdentifiableVO<Long, User> {

    public enum OrderBy {
        ID("id"),
        USERNAME("username"),
        EMAIL("email"),
        CREATED("created");

        private String field;

        OrderBy(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    private String username;
    private String email;
    private Collection<GrantedAuthority> authorities;
    private String password;
    private Locale locale;
    private Date lastLogin;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public Locale getLocale() {
        return locale;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UserVO)) return false;

        UserVO userVO = (UserVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(username, userVO.username)
                .append(email, userVO.email)
                .append(authorities, userVO.authorities)
                .append(password, userVO.password)
                .append(locale, userVO.locale)
                .append(lastLogin, userVO.lastLogin)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(username)
                .append(email)
                .append(authorities)
                .append(password)
                .append(locale)
                .append(lastLogin)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("created", created)
                .append("lastModified", lastModified)
                .append("enabled", enabled)
                .append("username", username)
                .append("email", email)
                .append("authorities", authorities)
                .append("locale", locale)
                .append("lastLogin", lastLogin)
                .toString();
    }

    public static UserVO wrapMinimumVO(Long id) {
        return getBuilder()
                .withId(id)
                .build();
    }

    public static UserVOBuilder getBuilder() {
        return new UserVOBuilder();
    }

    /**
     * Builder for {@link UserVO}.
     */
    public static final class UserVOBuilder {
        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String username;
        private String email;
        private Collection<GrantedAuthority> authorities;
        private String password;
        private Locale locale;
        private Date lastLogin;

        private UserVOBuilder() {
        }

        public UserVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public UserVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public UserVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserVOBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserVOBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserVOBuilder withAuthorities(Collection<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserVOBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserVOBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public UserVOBuilder withLastLogin(Date lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public UserVO build() {
            UserVO userVO = new UserVO();
            userVO.authorities = this.authorities;
            userVO.email = this.email;
            userVO.id = this.id;
            userVO.lastModified = this.lastModified;
            userVO.enabled = this.enabled;
            userVO.created = this.created;
            userVO.password = this.password;
            userVO.locale = this.locale;
            userVO.username = this.username;
            userVO.lastLogin = this.lastLogin;
            return userVO;
        }
    }
}
