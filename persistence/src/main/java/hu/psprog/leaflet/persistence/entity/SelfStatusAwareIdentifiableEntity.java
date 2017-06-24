package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Base entity class for entities that can be enabled/disabled and having created and last modified date fields.
 *
 * @author Peter Smith
 */
@MappedSuperclass
public abstract class SelfStatusAwareIdentifiableEntity<T extends Serializable> extends IdentifiableEntity<T> {

    @Column(name = DatabaseConstants.COLUMN_DATE_CREATED)
    private Date created;

    @Column(name = DatabaseConstants.COLUMN_DATE_LAST_MODIFIED)
    private Date lastModified;

    @Column(name = DatabaseConstants.COLUMN_IS_ENABLED)
    private boolean enabled;

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

        if (!(o instanceof SelfStatusAwareIdentifiableEntity)) return false;

        SelfStatusAwareIdentifiableEntity<?> that = (SelfStatusAwareIdentifiableEntity<?>) o;

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
                .appendSuper(super.hashCode())
                .append(created)
                .append(lastModified)
                .append(enabled)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("created", created)
                .append("lastModified", lastModified)
                .append("id", getId())
                .append("enabled", enabled)
                .toString();
    }
}
