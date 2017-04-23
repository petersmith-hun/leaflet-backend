package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

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

    public UploadedFile() {
        // Serializable
    }

    public UploadedFile(Long id, Date created, Date lastModified, boolean enabled, String path, String originalFilename, String mime) {
        super(id, created, lastModified, enabled);
        this.path = path;
        this.originalFilename = originalFilename;
        this.mime = mime;
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
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(path)
                .append(originalFilename)
                .append(mime)
                .toHashCode();
    }

    /**
     * Builder for {@link UploadedFile} entity.
     */
    public static final class Builder {
        private Date created;
        private String path;
        private Date lastModified;
        private String originalFilename;
        private boolean enabled;
        private Long id;
        private String mime;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
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

        public UploadedFile build() {
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setCreated(created);
            uploadedFile.setPath(path);
            uploadedFile.setLastModified(lastModified);
            uploadedFile.setOriginalFilename(originalFilename);
            uploadedFile.setEnabled(enabled);
            uploadedFile.setId(id);
            uploadedFile.setMime(mime);
            return uploadedFile;
        }
    }
}
