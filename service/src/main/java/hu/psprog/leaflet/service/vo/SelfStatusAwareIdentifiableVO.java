package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.SelfStatusAwareIdentifiableEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Value objects having fields for creation and last modification, and for status.
 *
 * @author Peter Smith
 */
public abstract class SelfStatusAwareIdentifiableVO<ID extends Serializable, T extends SelfStatusAwareIdentifiableEntity<ID>>
        extends IdentifiableVO<ID, T> {

    protected Date created;
    protected Date lastModified;
    protected boolean enabled;

    public Date getCreated() {
        return created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SelfStatusAwareIdentifiableVO)) return false;

        SelfStatusAwareIdentifiableVO<?, ?> that = (SelfStatusAwareIdentifiableVO<?, ?>) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(enabled, that.enabled)
                .append(created, that.created)
                .append(lastModified, that.lastModified)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(created)
                .append(lastModified)
                .append(enabled)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("created", created)
                .append("lastModified", lastModified)
                .append("enabled", enabled)
                .toString();
    }
}
