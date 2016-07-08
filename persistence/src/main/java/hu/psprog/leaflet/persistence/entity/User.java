package hu.psprog.leaflet.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * User entity class.
 *
 * @author Peter Smith
 */
@Entity
public class User extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = "username")
    @NotNull
    @Size(max = 255)
    private String username;

    @Column(name = "email")
    @NotNull
    @Size(max = 255)
    private String email;

    @Column(name = "role")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password")
    @Size(max = 255)
    private String password;

    @Column(name = "default_locale")
    @Enumerated(EnumType.STRING)
    private Locale defaultLocale;

    @Column(name = "date_last_login")
    private Date dateLastLogin;

    public User() {
        // Serializable
    }
}
