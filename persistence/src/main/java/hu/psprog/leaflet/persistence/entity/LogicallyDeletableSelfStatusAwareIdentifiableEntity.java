package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Logically deletable entity type. These entities have a boolean deleted field that marks an entity logically deleted.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
public abstract class LogicallyDeletableSelfStatusAwareIdentifiableEntity<T extends Serializable> extends SelfStatusAwareIdentifiableEntity<T> {

    @Column(name = DatabaseConstants.COLUMN_DELETED)
    private boolean deleted;
}
