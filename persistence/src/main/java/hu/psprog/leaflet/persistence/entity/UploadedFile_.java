package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link UploadedFile} entity.
 *
 * @author Peter Smith
 */
@StaticMetamodel(UploadedFile.class)
public class UploadedFile_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Tag, String> path;
    public static volatile SingularAttribute<Tag, String> originalFilename;
    public static volatile SingularAttribute<Tag, String> mime;
}
