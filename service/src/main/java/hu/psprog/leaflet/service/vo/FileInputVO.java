package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
                .append(relativePath)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("originalFilename", originalFilename)
                .append("contentType", contentType)
                .append("size", size)
                .append("fileContentStream", fileContentStream)
                .append("relativePath", relativePath)
                .append("description", description)
                .toString();
    }

    public static FileInputVOBuilder getBuilder() {
        return new FileInputVOBuilder();
    }

    /**
     * Builder for {@link FileInputVO}.
     */
    public static final class FileInputVOBuilder {
        private String originalFilename;
        private String contentType;
        private long size;
        private InputStream fileContentStream;
        private String relativePath;
        private String description;

        private FileInputVOBuilder() {
        }

        public FileInputVOBuilder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public FileInputVOBuilder withContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public FileInputVOBuilder withSize(long size) {
            this.size = size;
            return this;
        }

        public FileInputVOBuilder withFileContentStream(InputStream fileContentStream) {
            this.fileContentStream = fileContentStream;
            return this;
        }

        public FileInputVOBuilder withRelativePath(String relativePath) {
            this.relativePath = relativePath;
            return this;
        }

        public FileInputVOBuilder withDescription(String description) {
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
