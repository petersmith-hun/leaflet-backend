package hu.psprog.leaflet.persistence.entity.metamodel;

import hu.psprog.leaflet.persistence.entity.Document;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Document}.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Document.class)
public class Document_ {
    public static volatile SingularAttribute<Document, Boolean> enabled;
}
