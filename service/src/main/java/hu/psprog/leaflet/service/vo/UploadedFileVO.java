package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.UUID;

/**
 * Value object for uploaded files.
 *
 * @author Peter Smith
 */
public class UploadedFileVO extends SelfStatusAwareIdentifiableVO<Long, UploadedFile> {

    private String originalFilename;
    private String path;
    private String acceptedAs;
    private String storedFilename;
    private UUID pathUUID;
    private String description;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getPath() {
        return path;
    }

    public String getAcceptedAs() {
        return acceptedAs;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public UUID getPathUUID() {
        return pathUUID;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UploadedFileVO)) return false;

        UploadedFileVO that = (UploadedFileVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(originalFilename, that.originalFilename)
                .append(path, that.path)
                .append(acceptedAs, that.acceptedAs)
                .append(storedFilename, that.storedFilename)
                .append(pathUUID, that.pathUUID)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(originalFilename)
                .append(path)
                .append(acceptedAs)
                .append(storedFilename)
                .append(pathUUID)
                .append(description)
                .toHashCode();
    }

    /**
     * Builder for {@link UploadedFileVO}.
     */
    public static final class Builder {
        private Long id;
        private String originalFilename;
        private String path;
        private Date created;
        private String acceptedAs;
        private Date lastModified;
        private String storedFilename;
        private boolean enabled;
        private UUID pathUUID;
        private String description;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withAcceptedAs(String acceptedAs) {
            this.acceptedAs = acceptedAs;
            return this;
        }

        public Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder withStoredFilename(String storedFilename) {
            this.storedFilename = storedFilename;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withPathUUID(UUID pathUUID) {
            this.pathUUID = pathUUID;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public UploadedFileVO build() {
            UploadedFileVO uploadedFileVO = new UploadedFileVO();
            uploadedFileVO.setId(id);
            uploadedFileVO.setCreated(created);
            uploadedFileVO.setLastModified(lastModified);
            uploadedFileVO.setEnabled(enabled);
            uploadedFileVO.originalFilename = this.originalFilename;
            uploadedFileVO.acceptedAs = this.acceptedAs;
            uploadedFileVO.pathUUID = this.pathUUID;
            uploadedFileVO.path = this.path;
            uploadedFileVO.storedFilename = this.storedFilename;
            uploadedFileVO.description = this.description;
            return uploadedFileVO;
        }
    }
}
