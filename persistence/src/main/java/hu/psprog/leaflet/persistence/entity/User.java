package hu.psprog.leaflet.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

/**
 * User entity class.
 * <p>
 * Relations:
 *  - {@link User} 1:N {@link Comment}
 *  - {@link User} 1:N {@link Document}
 *  - {@link User} 1:N {@link Entry}
 *
 * @author Peter Smith
 */
@Immutable
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
@Entity
@Table(name = DatabaseConstants.TABLE_USERS,
        uniqueConstraints = @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_EMAIL, name = DatabaseConstants.UK_USER_EMAIL))
public class User extends IdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_USERNAME)
    @NotNull
    @Size(max = 255)
    private String username;

    @Column(name = DatabaseConstants.COLUMN_EMAIL)
    @NotNull
    @Size(max = 255)
    private String email;
}
