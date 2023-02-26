package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.LogicallyDeletableSelfStatusAwareIdentifiableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Value objects having field for logical deletion.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public abstract class LogicallyDeletableSelfStatusAwareIdentifiableVO<ID extends Serializable, T extends LogicallyDeletableSelfStatusAwareIdentifiableEntity<ID>>
        extends SelfStatusAwareIdentifiableVO<ID, T> {

    private final boolean deleted;
}
