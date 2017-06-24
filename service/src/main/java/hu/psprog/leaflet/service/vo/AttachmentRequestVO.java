package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;

/**
 * Attachment request value object.
 *
 * @author Peter Smith
 */
public class AttachmentRequestVO implements Serializable {

    private Long entryID;
    private UUID pathUUID;

    public Long getEntryID() {
        return entryID;
    }

    public UUID getPathUUID() {
        return pathUUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AttachmentRequestVO)) return false;

        AttachmentRequestVO that = (AttachmentRequestVO) o;

        return new EqualsBuilder()
                .append(entryID, that.entryID)
                .append(pathUUID, that.pathUUID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(entryID)
                .append(pathUUID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entryID", entryID)
                .append("pathUUID", pathUUID)
                .toString();
    }

    public static AttachmentRequestBuilder getBuilder() {
        return new AttachmentRequestBuilder();
    }

    /**
     * Builder for {@link AttachmentRequestVO}.
     */
    public static final class AttachmentRequestBuilder {
        private Long entryID;
        private UUID pathUUID;

        private AttachmentRequestBuilder() {
        }

        public AttachmentRequestBuilder withEntryID(Long entryID) {
            this.entryID = entryID;
            return this;
        }

        public AttachmentRequestBuilder withPathUUID(UUID pathUUID) {
            this.pathUUID = pathUUID;
            return this;
        }

        public AttachmentRequestVO build() {
            AttachmentRequestVO attachmentRequestVO = new AttachmentRequestVO();
            attachmentRequestVO.pathUUID = this.pathUUID;
            attachmentRequestVO.entryID = this.entryID;
            return attachmentRequestVO;
        }
    }
}
