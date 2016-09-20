package hu.psprog.leaflet.persistence.entity.metamodel;

import hu.psprog.leaflet.persistence.entity.Comment;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Comment} class.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Comment.class)
public class Comment_ {
    public static volatile SingularAttribute<Comment, Boolean> enabled;
}
