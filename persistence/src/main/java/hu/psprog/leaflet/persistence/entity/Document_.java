package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Document}.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Document.class)
public class Document_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Document, String> rawContent;
    public static volatile SingularAttribute<Document, String> link;
    public static volatile SingularAttribute<Document, Locale> locale;
    public static volatile SingularAttribute<Document, String> title;
    public static volatile SingularAttribute<Document, String> seoTitle;
    public static volatile SingularAttribute<Document, String> seoDescription;
    public static volatile SingularAttribute<Document, String> seoKeywords;
    public static volatile SingularAttribute<Document, User> user;
}
