package hu.psprog.leaflet.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Value objects having field for logical deletion.
 *
 * @author Peter Smith
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public abstract class LogicallyDeletableSelfStatusAwareIdentifiableVO<ID extends Serializable>
        extends SelfStatusAwareIdentifiableVO<ID> {

    private final boolean deleted;
}
