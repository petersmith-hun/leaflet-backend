package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Entry} class.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Entry.class)
public class Entry_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Entry, EntryStatus> status;
    public static volatile SingularAttribute<Entry, String> content;
    public static volatile SingularAttribute<Entry, String> rawContent;
    public static volatile SingularAttribute<Entry, String> prologue;
    public static volatile SingularAttribute<Entry, String> link;
    public static volatile SingularAttribute<Entry, Locale> locale;
    public static volatile SingularAttribute<Entry, String> title;
    public static volatile SingularAttribute<Entry, String> seoTitle;
    public static volatile SingularAttribute<Entry, String> seoDescription;
    public static volatile SingularAttribute<Entry, String> seoKeywords;
    public static volatile SingularAttribute<Entry, User> user;
    public static volatile SingularAttribute<Entry, Category> category;
    public static volatile ListAttribute<Entry, Tag> tags;
}
