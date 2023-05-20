package hu.psprog.leaflet.persistence.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Tag} entity.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Tag.class)
public class Tag_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Tag, String> title;
}
