package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Value object for uploaded files.
 *
 * @author Peter Smith
 */
public class UploadedFileVO {

    private String originalFilename;
    private String storedFilename;
    private String path;
    private String acceptedAs;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
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
                .append(storedFilename, that.storedFilename)
                .append(path, that.path)
                .append(acceptedAs, that.acceptedAs)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(originalFilename)
                .append(storedFilename)
                .append(path)
                .append(acceptedAs)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("originalFilename", originalFilename)
                .append("storedFilename", storedFilename)
                .append("path", path)
                .append("acceptedAs", acceptedAs)
                .toString();
    }


    public static final class Builder {
        private String originalFilename;
        private String storedFilename;
        private String path;
        private String acceptedAs;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public Builder withStoredFilename(String storedFilename) {
            this.storedFilename = storedFilename;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withAcceptedAs(String acceptedAs) {
            this.acceptedAs = acceptedAs;
            return this;
        }

        public UploadedFileVO build() {
            UploadedFileVO uploadedFileVO = new UploadedFileVO();
            uploadedFileVO.acceptedAs = this.acceptedAs;
            uploadedFileVO.storedFilename = this.storedFilename;
            uploadedFileVO.path = this.path;
            uploadedFileVO.originalFilename = this.originalFilename;
            return uploadedFileVO;
        }
    }
}
