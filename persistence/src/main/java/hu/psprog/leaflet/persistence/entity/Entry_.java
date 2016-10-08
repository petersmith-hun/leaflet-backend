package hu.psprog.leaflet.persistence.entity;

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
}
