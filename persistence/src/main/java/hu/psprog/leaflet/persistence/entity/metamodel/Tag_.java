package hu.psprog.leaflet.persistence.entity.metamodel;

import hu.psprog.leaflet.persistence.entity.Tag;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Tag} entity.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Tag.class)
public class Tag_ {
    public static volatile SingularAttribute<Tag, Boolean> enabled;
}
