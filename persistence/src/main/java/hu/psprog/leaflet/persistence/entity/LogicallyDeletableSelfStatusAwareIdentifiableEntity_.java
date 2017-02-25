package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Static metamodel for {@link LogicallyDeletableSelfStatusAwareIdentifiableEntity}.
 *
 * @author Peter Smith
 */
@StaticMetamodel(LogicallyDeletableSelfStatusAwareIdentifiableEntity.class)
public class LogicallyDeletableSelfStatusAwareIdentifiableEntity_ extends SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<LogicallyDeletableSelfStatusAwareIdentifiableEntity, Boolean> deleted;
}
