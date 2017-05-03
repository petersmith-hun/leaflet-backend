package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * Entity class for uploaded files' meta information.
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_UPLOADED_FILES)
public class UploadedFile extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_PATH, unique = true)
    private String path;

    @Column(name = DatabaseConstants.COLUMN_ORIGINAL_FILENAME)
    private String originalFilename;

    @Column(name = DatabaseConstants.COLUMN_MIME)
    private String mime;

    @Column(name = DatabaseConstants.COLUMN_PATH_UUID, unique = true)
    private UUID pathUUID;

    @Column(name = DatabaseConstants.COLUMN_STORED_FILENAME)
    private String storedFilename;

    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION)
    private String description;

    public UploadedFile() {
        // Serializable
    }

    public UploadedFile(Long id, Date created, Date lastModified, boolean enabled, String path, String originalFilename,
                        String mime, UUID pathUUID, String storedFilename, String description) {
        super(id, created, lastModified, enabled);
        this.path = path;
        this.originalFilename = originalFilename;
        this.mime = mime;
        this.pathUUID = pathUUID;
        this.storedFilename = storedFilename;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public UUID getPathUUID() {
        return pathUUID;
    }

    public void setPathUUID(UUID pathUUID) {
        this.pathUUID = pathUUID;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UploadedFile)) return false;

        UploadedFile that = (UploadedFile) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(path, that.path)
                .append(originalFilename, that.originalFilename)
                .append(mime, that.mime)
                .append(pathUUID, that.pathUUID)
                .append(storedFilename, that.storedFilename)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(path)
                .append(originalFilename)
                .append(mime)
                .append(pathUUID)
                .append(storedFilename)
                .append(description)
                .toHashCode();
    }

    /**
     * Builder for {@link UploadedFile} entity.
     */
    public static final class Builder {
        private Date created;
        private Date lastModified;
        private String path;
        private boolean enabled;
        private String originalFilename;
        private Long id;
        private String mime;
        private UUID pathUUID;
        private String storedFilename;
        private String description;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withMime(String mime) {
            this.mime = mime;
            return this;
        }

        public Builder withPathUUID(UUID pathUUID) {
            this.pathUUID = pathUUID;
            return this;
        }

        public Builder withStoredFilename(String storedFilename) {
            this.storedFilename = storedFilename;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public UploadedFile build() {
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setCreated(created);
            uploadedFile.setLastModified(lastModified);
            uploadedFile.setPath(path);
            uploadedFile.setEnabled(enabled);
            uploadedFile.setOriginalFilename(originalFilename);
            uploadedFile.setId(id);
            uploadedFile.setMime(mime);
            uploadedFile.setPathUUID(pathUUID);
            uploadedFile.setStoredFilename(storedFilename);
            uploadedFile.setDescription(description);
            return uploadedFile;
        }
    }
}
