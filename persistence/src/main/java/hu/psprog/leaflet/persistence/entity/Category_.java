package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link Category} class.
 *
 * @author Peter Smith
 */
@StaticMetamodel(Category.class)
public class Category_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<Category, String> title;
    public static volatile SingularAttribute<Category, String> description;
}
