package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.core.io.ByteArrayResource;

/**
 * VO to provide a downloadable file and its meta information for downloads.
 *
 * @author Peter Smith
 */
public class DownloadableFileWrapperVO {

    private ByteArrayResource fileContent;
    private String originalFilename;
    private String mimeType;
    private long length;

    public ByteArrayResource getFileContent() {
        return fileContent;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof DownloadableFileWrapperVO)) return false;

        DownloadableFileWrapperVO that = (DownloadableFileWrapperVO) o;

        return new EqualsBuilder()
                .append(length, that.length)
                .append(fileContent, that.fileContent)
                .append(originalFilename, that.originalFilename)
                .append(mimeType, that.mimeType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(fileContent)
                .append(originalFilename)
                .append(mimeType)
                .append(length)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("originalFilename", originalFilename)
                .append("mimeType", mimeType)
                .append("length", length)
                .toString();
    }

    public static DownloadableFileWrapperVOBuilder getBuilder() {
        return new DownloadableFileWrapperVOBuilder();
    }

    /**
     * Builder for {@link DownloadableFileWrapperVO}.
     */
    public static final class DownloadableFileWrapperVOBuilder {
        private ByteArrayResource fileContent;
        private String originalFilename;
        private String mimeType;
        private long length;

        private DownloadableFileWrapperVOBuilder() {
        }

        public DownloadableFileWrapperVOBuilder withFileContent(ByteArrayResource fileContent) {
            this.fileContent = fileContent;
            return this;
        }

        public DownloadableFileWrapperVOBuilder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public DownloadableFileWrapperVOBuilder withMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public DownloadableFileWrapperVOBuilder withLength(long length) {
            this.length = length;
            return this;
        }

        public DownloadableFileWrapperVO build() {
            DownloadableFileWrapperVO downloadableFileWrapperVO = new DownloadableFileWrapperVO();
            downloadableFileWrapperVO.fileContent = this.fileContent;
            downloadableFileWrapperVO.originalFilename = this.originalFilename;
            downloadableFileWrapperVO.mimeType = this.mimeType;
            downloadableFileWrapperVO.length = this.length;
            return downloadableFileWrapperVO;
        }
    }
}
