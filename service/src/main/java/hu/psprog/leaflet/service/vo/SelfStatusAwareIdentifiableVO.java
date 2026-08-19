package hu.psprog.leaflet.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Value objects having fields for creation and last modification, and for status.
 *
 * @author Peter Smith
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public abstract class SelfStatusAwareIdentifiableVO<ID extends Serializable> extends IdentifiableVO<ID> {

    private final Date created;
    private final Date lastModified;
    private final boolean enabled;
}
