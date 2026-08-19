package hu.psprog.leaflet.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Value object for uploaded files.
 *
 * @author Peter Smith
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class UploadedFileVO extends SelfStatusAwareIdentifiableVO<Long> {

    private final String originalFilename;
    private final String path;
    private final String acceptedAs;
    private final String storedFilename;
    private final UUID pathUUID;
    private final String description;
}
