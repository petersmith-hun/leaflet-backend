package hu.psprog.leaflet.persistence.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Attachment entity class.
 *
 * Relations:
 *  - {@link Attachment} N:1 {@link Entry}
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_ATTACHMENTS)
public class Attachment extends SelfStatusAwareIdentifiableEntity<Long> {

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_ATTACHMENT_ENTRY))
    private Entry entry;

    @Column(name = DatabaseConstants.COLUMN_TITLE)
    private String title;

    @Column(name = DatabaseConstants.COLUMN_FILENAME, unique = true)
    private String filename;

    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION)
    private String description;

    @Column(name = DatabaseConstants.COLUMN_TYPE)
    private String type;

    @Column(name = DatabaseConstants.COLUMN_IS_PROTECTED)
    private boolean isProtected;

    public Attachment() {
        // Serializable
    }

    public Attachment(Long id, Date created, Date lastModified, boolean enabled, Entry entry, String title,
                      String filename, String description, String type, boolean isProtected) {
        super(id, created, lastModified, enabled);
        this.entry = entry;
        this.title = title;
        this.filename = filename;
        this.description = description;
        this.type = type;
        this.isProtected = isProtected;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Attachment entity builder.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private Entry entry;
        private String title;
        private String filename;
        private String description;
        private String type;
        private boolean isProtected;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withEntry(Entry entry) {
            this.entry = entry;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder isProtected(boolean isProtected) {
            this.isProtected = isProtected;
            return this;
        }

        public Attachment createAttachment() {
            return new Attachment(id, created, lastModified, enabled, entry, title, filename, description,
                    type, isProtected);
        }
    }
}
