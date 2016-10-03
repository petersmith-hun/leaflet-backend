package hu.psprog.leaflet.persistence.entity.metamodel;

import hu.psprog.leaflet.persistence.entity.Attachment;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Attachment}.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Attachment.class)
public class Attachment_ {
    public static volatile SingularAttribute<Attachment, Boolean> enabled;
}
