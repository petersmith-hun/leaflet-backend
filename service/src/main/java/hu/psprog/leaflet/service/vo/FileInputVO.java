package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.InputStream;

/**
 * Value object to store information of a file being uploaded (for further processing).
 *
 * @author Peter Smith
 */
public class FileInputVO {

    private String originalFilename;
    private String contentType;
    private long size;
    private InputStream fileContentStream;
    private String relativePath;
    private String description;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public InputStream getFileContentStream() {
        return fileContentStream;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof FileInputVO)) return false;

        FileInputVO that = (FileInputVO) o;

        return new EqualsBuilder()
                .append(size, that.size)
                .append(originalFilename, that.originalFilename)
                .append(contentType, that.contentType)
                .append(fileContentStream, that.fileContentStream)
                .append(relativePath, that.relativePath)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(originalFilename)
                .append(contentType)
                .append(size)
                .append(fileContentStream)
                .append(relativePath)
                .append(description)
                .toHashCode();
    }

    /**
     * Builder for {@link FileInputVO}.
     */
    public static final class Builder {
        private String originalFilename;
        private String contentType;
        private long size;
        private InputStream fileContentStream;
        private String relativePath;
        private String description;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public Builder withContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder withSize(long size) {
            this.size = size;
            return this;
        }

        public Builder withFileContentStream(InputStream fileContentStream) {
            this.fileContentStream = fileContentStream;
            return this;
        }

        public Builder withRelativePath(String relativePath) {
            this.relativePath = relativePath;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public FileInputVO build() {
            FileInputVO fileInputVO = new FileInputVO();
            fileInputVO.originalFilename = this.originalFilename;
            fileInputVO.contentType = this.contentType;
            fileInputVO.size = this.size;
            fileInputVO.fileContentStream = this.fileContentStream;
            fileInputVO.relativePath = this.relativePath;
            fileInputVO.description = this.description;
            return fileInputVO;
        }
    }
}
