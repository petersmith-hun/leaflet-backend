package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Value object for updating meta information of an existing file.
 *
 * @author Peter Smith
 */
public class UpdateFileMetaInfoVO implements Serializable {

    private String originalFilename;
    private String description;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UpdateFileMetaInfoVO)) return false;

        UpdateFileMetaInfoVO that = (UpdateFileMetaInfoVO) o;

        return new EqualsBuilder()
                .append(originalFilename, that.originalFilename)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(originalFilename)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("originalFilename", originalFilename)
                .append("description", description)
                .toString();
    }

    public static UpdateFileMetaInfoVOBuilder getBuilder() {
        return new UpdateFileMetaInfoVOBuilder();
    }

    /**
     * Builder for {@link UpdateFileMetaInfoVO} class.
     */
    public static final class UpdateFileMetaInfoVOBuilder {
        private String originalFilename;
        private String description;

        private UpdateFileMetaInfoVOBuilder() {
        }

        public UpdateFileMetaInfoVOBuilder withOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public UpdateFileMetaInfoVOBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public UpdateFileMetaInfoVO build() {
            UpdateFileMetaInfoVO updateFileMetaInfoVO = new UpdateFileMetaInfoVO();
            updateFileMetaInfoVO.originalFilename = this.originalFilename;
            updateFileMetaInfoVO.description = this.description;
            return updateFileMetaInfoVO;
        }
    }
}
