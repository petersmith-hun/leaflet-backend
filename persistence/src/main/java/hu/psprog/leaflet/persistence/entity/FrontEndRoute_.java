package hu.psprog.leaflet.persistence.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for {@link FrontEndRoute}.
 *
 * @author Peter Smith
 */
@StaticMetamodel(FrontEndRoute.class)
public class FrontEndRoute_ extends SelfStatusAwareIdentifiableEntity_ {

    public static volatile SingularAttribute<FrontEndRoute, String> routeId;
    public static volatile SingularAttribute<FrontEndRoute, String> name;
    public static volatile SingularAttribute<FrontEndRoute, String> url;
    public static volatile SingularAttribute<FrontEndRoute, Integer> sequenceNumber;
    public static volatile SingularAttribute<FrontEndRoute, FrontEndRouteType> type;
    public static volatile SingularAttribute<FrontEndRoute, FrontEndRouteAuthRequirement> authRequirement;
}
