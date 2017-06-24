package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.LogicallyDeletableSelfStatusAwareIdentifiableEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Value objects having field for logical deletion.
 *
 * @author Peter Smith
 */
public abstract class LogicallyDeletableSelfStatusAwareIdentifiableVO<ID extends Serializable, T extends LogicallyDeletableSelfStatusAwareIdentifiableEntity<ID>>
        extends SelfStatusAwareIdentifiableVO<ID, T> {

    protected boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof LogicallyDeletableSelfStatusAwareIdentifiableVO)) return false;

        LogicallyDeletableSelfStatusAwareIdentifiableVO<?, ?> that = (LogicallyDeletableSelfStatusAwareIdentifiableVO<?, ?>) o;

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
                .append("id", getId())
                .append("deleted", deleted)
                .append("created", getCreated())
                .append("lastModified", getLastModified())
                .append("enabled", isEnabled())
                .toString();
    }
}
