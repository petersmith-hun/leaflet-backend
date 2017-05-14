package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.SelfStatusAwareIdentifiableEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Value objects having fields for creation and last modification, and for status.
 *
 * @author Peter Smith
 */
public class SelfStatusAwareIdentifiableVO<ID extends Serializable, T extends SelfStatusAwareIdentifiableEntity<ID>>
        extends IdentifiableVO<ID, T> {

    private Date created;
    private Date lastModified;
    private boolean enabled;

    public SelfStatusAwareIdentifiableVO() {
        // Serializable
    }

    public SelfStatusAwareIdentifiableVO(ID id, Date created, Date lastModified, boolean enabled) {
        super(id);
        this.created = created;
        this.lastModified = lastModified;
        this.enabled = enabled;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
}
