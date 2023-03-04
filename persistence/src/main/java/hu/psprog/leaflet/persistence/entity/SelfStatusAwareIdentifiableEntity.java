package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Base entity class for entities that can be enabled/disabled and having created and last modified date fields.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
public abstract class SelfStatusAwareIdentifiableEntity<T extends Serializable> extends IdentifiableEntity<T> {

    @Column(name = DatabaseConstants.COLUMN_DATE_CREATED)
    private Date created;

    @Column(name = DatabaseConstants.COLUMN_DATE_LAST_MODIFIED)
    private Date lastModified;

    @Column(name = DatabaseConstants.COLUMN_IS_ENABLED)
    private boolean enabled;
}
