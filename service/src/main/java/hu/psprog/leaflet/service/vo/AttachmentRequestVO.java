package hu.psprog.leaflet.service.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * Attachment request value object.
 *
 * @author Peter Smith
 */
@Data
@Builder(builderMethodName = "getBuilder", setterPrefix = "with")
public class AttachmentRequestVO implements Serializable {

    private final Long entryID;
    private final UUID pathUUID;
}
