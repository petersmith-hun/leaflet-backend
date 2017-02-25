package hu.psprog.leaflet.persistence.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Logically deletable entity type. These entities have a boolean deleted field that marks an entity logically deleted.
 *
 * @author Peter Smith
 */
@MappedSuperclass
public class LogicallyDeletableSelfStatusAwareIdentifiableEntity<T extends Serializable> extends SelfStatusAwareIdentifiableEntity<T> {

    @Column(name = DatabaseConstants.COLUMN_DELETED)
    private boolean deleted;

    public LogicallyDeletableSelfStatusAwareIdentifiableEntity() {
        // Serializable
    }

    public LogicallyDeletableSelfStatusAwareIdentifiableEntity(T id, Date created, Date lastModified, boolean enabled, boolean deleted) {
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
