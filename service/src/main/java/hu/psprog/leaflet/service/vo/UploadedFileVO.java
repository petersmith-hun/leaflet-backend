package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Value object for uploaded files.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class UploadedFileVO extends SelfStatusAwareIdentifiableVO<Long, UploadedFile> {

    private final String originalFilename;
    private final String path;
    private final String acceptedAs;
    private final String storedFilename;
    private final UUID pathUUID;
    private final String description;
}
