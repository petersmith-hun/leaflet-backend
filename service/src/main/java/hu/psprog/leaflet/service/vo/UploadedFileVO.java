package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Value object for uploaded files.
 *
 * @author Peter Smith
 */
public class UploadedFileVO extends SelfStatusAwareIdentifiableVO<Long, UploadedFile> {

    private String originalFilename;
    private String path;
    private String acceptedAs;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getPath() {
        return path;
    }

    public String getAcceptedAs() {
        return acceptedAs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UploadedFileVO)) return false;

        UploadedFileVO that = (UploadedFileVO) o;

        return new EqualsBuilder()
                .append(originalFilename, that.originalFilename)
                .append(path, that.path)
                .append(acceptedAs, that.acceptedAs)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(originalFilename)
                .append(path)
                .append(acceptedAs)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("originalFilename", originalFilename)
                .append("path", path)
                .append("acceptedAs", acceptedAs)
                .toString();
    }

    /**
     * Builder for {@link UploadedFileVO}.
     */
    public static final class Builder {
        private Long id;
        private String originalFilename;
        private Date created;
        private String path;
        private Date lastModified;
        private String acceptedAs;
        private boolean enabled;

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

        public Builder withAcceptedAs(String acceptedAs) {
            this.acceptedAs = acceptedAs;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UploadedFileVO build() {
            UploadedFileVO uploadedFileVO = new UploadedFileVO();
            uploadedFileVO.setId(id);
            uploadedFileVO.setCreated(created);
            uploadedFileVO.setLastModified(lastModified);
            uploadedFileVO.setEnabled(enabled);
            uploadedFileVO.acceptedAs = this.acceptedAs;
            uploadedFileVO.originalFilename = this.originalFilename;
            uploadedFileVO.path = this.path;
            return uploadedFileVO;
        }
    }
}
