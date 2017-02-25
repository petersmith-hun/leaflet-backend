package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.LogicallyDeletableSelfStatusAwareIdentifiableEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * Value objects having field for logical deletion.
 *
 * @author Peter Smith
 */
public class LogicallyDeletableSelfStatusAwareIdentifiableVO<ID extends Serializable, T extends LogicallyDeletableSelfStatusAwareIdentifiableEntity<ID>>
        extends SelfStatusAwareIdentifiableVO<ID, T> {

    private boolean deleted;

    public LogicallyDeletableSelfStatusAwareIdentifiableVO() {
        // Serializable
    }

    public LogicallyDeletableSelfStatusAwareIdentifiableVO(ID id, Date created, Date lastModified, boolean enabled, boolean deleted) {
        super(id, created, lastModified, enabled);
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
