package hu.psprog.leaflet.persistence.entity.metamodel;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Entry}
 *
 * @author Peter Smith
 */
@StaticMetamodel(Entry.class)
public class Entry_ {
    public static volatile SingularAttribute<Entry, EntryStatus> status;
}
