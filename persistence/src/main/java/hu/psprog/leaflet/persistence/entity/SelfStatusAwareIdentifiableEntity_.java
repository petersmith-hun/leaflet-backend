package hu.psprog.leaflet.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author Peter Smith
 */
@StaticMetamodel(SelfStatusAwareIdentifiableEntity.class)
public class SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<SelfStatusAwareIdentifiableEntity, Boolean> enabled;
}
