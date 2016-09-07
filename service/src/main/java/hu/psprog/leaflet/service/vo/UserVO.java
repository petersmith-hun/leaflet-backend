package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * VO for {@link User} entity.
 *
 * @author Peter Smith
 */
public class UserVO extends SelfStatusAwareIdentifiableVO<Long, User> implements UserDetails {

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

    public UserVO() {
        // Serializable
    }

    public UserVO(Long id, Date created, Date lastModified, boolean enabled, String username, String email,
                  Collection<GrantedAuthority> authorities, String password, Locale locale) {
        super(id, created, lastModified, enabled);
        this.username = username;
        this.email = email;
        this.authorities = authorities;
        this.password = password;
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UserVO that = (UserVO) o;

        return email.equals(that.email);

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String username;
        private String email;
        private Collection<GrantedAuthority> authorities;
        private String password;
        private Locale locale;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withAuthorities(Collection<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public UserVO createUserVO() {
            return new UserVO(id, created, lastModified, enabled, username, email, authorities, password, locale);
        }
    }
}
