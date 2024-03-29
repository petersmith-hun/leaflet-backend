package hu.psprog.leaflet.persistence.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Comment} class.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Comment.class)
public class Comment_ extends LogicallyDeletableSelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Comment, Entry> entry;
    public static volatile SingularAttribute<Comment, String> content;
    public static volatile SingularAttribute<Comment, User> user;
}
