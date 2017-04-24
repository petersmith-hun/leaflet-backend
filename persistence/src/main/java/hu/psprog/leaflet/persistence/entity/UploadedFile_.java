package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.UUID;

/**
 * Metamodel for {@link UploadedFile} entity.
 *
 * @author Peter Smith
 */
@StaticMetamodel(UploadedFile.class)
public class UploadedFile_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<UploadedFile, String> path;
    public static volatile SingularAttribute<UploadedFile, String> originalFilename;
    public static volatile SingularAttribute<UploadedFile, String> mime;
    public static volatile SingularAttribute<UploadedFile, UUID> pathUUID;
    public static volatile SingularAttribute<UploadedFile, String> storedFilename;
    public static volatile SingularAttribute<UploadedFile, String> description;
}
