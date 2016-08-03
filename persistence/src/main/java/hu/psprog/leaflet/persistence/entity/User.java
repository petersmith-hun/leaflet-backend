package hu.psprog.leaflet.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
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
@Table(name = DatabaseConstants.TABLE_USERS)
public class User extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_USERNAME)
    @NotNull
    @Size(max = 255)
    private String username;

    @Column(name = DatabaseConstants.COLUMN_EMAIL, unique = true)
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

    public User() {
        // Serializable
    }

    public User(Long id, Date created, Date lastModified, boolean enabled, String username, String email, Role role,
                String password, Locale defaultLocale, Date lastLogin) {
        super(id, created, lastModified, enabled);
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
        this.defaultLocale = defaultLocale;
        this.lastLogin = lastLogin;
    }

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
    public String toString() {
        return super.toString();
    }

    /**
     * User entity builder.
     */
    public static class Builder {


        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String username;
        private String email;
        private Role role;
        private String password;
        private Locale defaultLocale;
        private Date lastLogin;

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

        public Builder isEnabled(boolean enabled) {
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

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withDefaultLocale(Locale defaultLocale) {
            this.defaultLocale = defaultLocale;
            return this;
        }

        public Builder withLastLogin(Date lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public User createUser() {
            return new User(id, created, lastModified, enabled, username, email, role, password,
                    defaultLocale, lastLogin);
        }
    }
}
