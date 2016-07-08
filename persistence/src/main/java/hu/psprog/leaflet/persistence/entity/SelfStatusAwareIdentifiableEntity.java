package hu.psprog.leaflet.persistence.entity;

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
public class SelfStatusAwareIdentifiableEntity<T extends Serializable> extends IdentifiableEntity<T> {

    @Column(name = "date_created")
    private Date created;

    @Column(name = "date_last_modified")
    private Date lastModified;

    @Column(name = "is_enabled")
    private boolean enabled;

    public SelfStatusAwareIdentifiableEntity() {
        // Serializable
    }

    public SelfStatusAwareIdentifiableEntity(Date created, Date lastModified, boolean enabled) {
        this.created = created;
        this.lastModified = lastModified;
        this.enabled = enabled;
    }

    public SelfStatusAwareIdentifiableEntity(T id, Date created, Date lastModified, boolean enabled) {
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
}
