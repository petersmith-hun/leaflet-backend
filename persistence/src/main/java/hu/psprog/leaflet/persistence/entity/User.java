package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * User entity class.
 *
 * Relations:
 *  - {@link User} 1:N {@link Comment}
 *  - {@link User} 1:N {@link Document}
 *  - {@link User} 1:N {@link Entry}
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_USERS,
        uniqueConstraints = @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_EMAIL, name = DatabaseConstants.UK_USER_EMAIL))
public class User extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_USERNAME)
    @NotNull
    @Size(max = 255)
    private String username;

    @Column(name = DatabaseConstants.COLUMN_EMAIL)
    @NotNull
    @Size(max = 255)
    private String email;

    @Column(name = DatabaseConstants.COLUMN_ROLE)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = DatabaseConstants.COLUMN_PASSWORD)
    @Size(max = 255)
    private String password;

    @Column(name = DatabaseConstants.COLUMN_DEFAULT_LOCALE)
    @Enumerated(EnumType.STRING)
    private Locale defaultLocale;

    @Column(name = DatabaseConstants.COLUMN_DATE_LAST_LOGIN)
    private Date lastLogin;

    public String getUsername() {
        return username;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof User)) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(username, user.username)
                .append(email, user.email)
                .append(role, user.role)
                .append(password, user.password)
                .append(defaultLocale, user.defaultLocale)
                .append(lastLogin, user.lastLogin)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(username)
                .append(email)
                .append(role)
                .append(password)
                .append(defaultLocale)
                .append(lastLogin)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("username", username)
                .append("created", getCreated())
                .append("email", email)
                .append("lastModified", getLastModified())
                .append("role", role)
                .append("enabled", isEnabled())
                .append("defaultLocale", defaultLocale)
                .append("lastLogin", lastLogin)
                .toString();
    }

    public static UserBuilder getBuilder() {
        return new UserBuilder();
    }

    /**
     * User entity builder.
     */
    public static final class UserBuilder {
        private Date created;
        private Long id;
        private Date lastModified;
        private boolean enabled;
        private String username;
        private String email;
        private Role role;
        private String password;
        private Locale defaultLocale;
        private Date lastLogin;

        private UserBuilder() {
        }

        public UserBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public UserBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withRole(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withDefaultLocale(Locale defaultLocale) {
            this.defaultLocale = defaultLocale;
            return this;
        }

        public UserBuilder withLastLogin(Date lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public User build() {
            User user = new User();
            user.setCreated(created);
            user.setId(id);
            user.setLastModified(lastModified);
            user.setEnabled(enabled);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(role);
            user.setPassword(password);
            user.setDefaultLocale(defaultLocale);
            user.setLastLogin(lastLogin);
            return user;
        }
    }
}
