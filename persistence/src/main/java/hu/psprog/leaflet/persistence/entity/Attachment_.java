package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Attachment}.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Attachment.class)
public class Attachment_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Attachment, Entry> entry;
    public static volatile SingularAttribute<Attachment, String> title;
    public static volatile SingularAttribute<Attachment, String> filename;
    public static volatile SingularAttribute<Attachment, String> description;
    public static volatile SingularAttribute<Attachment, String> type;
    public static volatile SingularAttribute<Attachment, Boolean> isProtected;
}
