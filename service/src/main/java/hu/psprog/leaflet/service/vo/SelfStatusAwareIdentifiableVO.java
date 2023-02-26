package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.SelfStatusAwareIdentifiableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Value objects having fields for creation and last modification, and for status.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public abstract class SelfStatusAwareIdentifiableVO<ID extends Serializable, T extends SelfStatusAwareIdentifiableEntity<ID>> extends IdentifiableVO<ID> {

    private final Date created;
    private final Date lastModified;
    private final boolean enabled;
}
