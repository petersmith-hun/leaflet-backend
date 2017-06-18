package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Logically deletable entity type. These entities have a boolean deleted field that marks an entity logically deleted.
 *
 * @author Peter Smith
 */
@MappedSuperclass
public abstract class LogicallyDeletableSelfStatusAwareIdentifiableEntity<T extends Serializable> extends SelfStatusAwareIdentifiableEntity<T> {

    @Column(name = DatabaseConstants.COLUMN_DELETED)
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof LogicallyDeletableSelfStatusAwareIdentifiableEntity)) return false;

        LogicallyDeletableSelfStatusAwareIdentifiableEntity<?> that = (LogicallyDeletableSelfStatusAwareIdentifiableEntity<?>) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(deleted, that.deleted)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(deleted)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("deleted", deleted)
                .append("id", getId())
                .append("created", getCreated())
                .append("lastModified", getLastModified())
                .append("enabled", isEnabled())
                .toString();
    }
}
