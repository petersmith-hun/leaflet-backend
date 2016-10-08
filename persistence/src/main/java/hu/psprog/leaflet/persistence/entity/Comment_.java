package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Comment} class.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Comment.class)
public class Comment_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Comment, Entry> entry;
}
