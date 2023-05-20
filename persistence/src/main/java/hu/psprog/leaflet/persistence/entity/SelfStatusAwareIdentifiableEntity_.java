package hu.psprog.leaflet.persistence.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * @author Peter Smith
 */
@StaticMetamodel(SelfStatusAwareIdentifiableEntity.class)
public class SelfStatusAwareIdentifiableEntity_ {
    public static volatile SingularAttribute<SelfStatusAwareIdentifiableEntity, Boolean> enabled;
    public static volatile SingularAttribute<SelfStatusAwareIdentifiableEntity, Date> created;
    public static volatile SingularAttribute<SelfStatusAwareIdentifiableEntity, Date> lastModified;
}
