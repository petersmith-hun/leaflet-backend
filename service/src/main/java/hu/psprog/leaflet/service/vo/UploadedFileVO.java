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

    public static UploadedFileVOBuilder getBuilder() {
        return new UploadedFileVOBuilder();
    }

    /**
     * Builder for {@link UploadedFileVO}.
     */
    public static final class UploadedFileVOBuilder {
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

        private UploadedFileVOBuilder() {
        }

        public UploadedFileVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UploadedFileVOBuilder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public UploadedFileVOBuilder withPath(String path) {
            this.path = path;
            return this;
        }

        public UploadedFileVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public UploadedFileVOBuilder withAcceptedAs(String acceptedAs) {
            this.acceptedAs = acceptedAs;
            return this;
        }

        public UploadedFileVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public UploadedFileVOBuilder withStoredFilename(String storedFilename) {
            this.storedFilename = storedFilename;
            return this;
        }

        public UploadedFileVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UploadedFileVOBuilder withPathUUID(UUID pathUUID) {
            this.pathUUID = pathUUID;
            return this;
        }

        public UploadedFileVOBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public UploadedFileVO build() {
            UploadedFileVO uploadedFileVO = new UploadedFileVO();
            uploadedFileVO.id = id;
            uploadedFileVO.created = created;
            uploadedFileVO.lastModified = lastModified;
            uploadedFileVO.enabled = enabled;
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
