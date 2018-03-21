package hu.psprog.leaflet.persistence.repository.specification;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.persistence.entity.FrontEndRoute_;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter specifications for {@link FrontEndRoute} entity.
 *
 * @author Peter Smith
 */
public class FrontEndRouteSpecification {

    private FrontEndRouteSpecification() {
        // prevent initialization
    }

    /**
     * Filter to list routes marked as enabled.
     */
    public static final Specification<FrontEndRoute> IS_ENABLED = (root, query, builder) -> builder.equal(root.get(FrontEndRoute_.enabled), true);

    /**
     * Filter to list routes of type {@link FrontEndRouteType#HEADER_MENU}.
     */
    public static final Specification<FrontEndRoute> IS_HEADER = (root, query, builder) -> builder.equal(root.get(FrontEndRoute_.type), FrontEndRouteType.HEADER_MENU);

    /**
     * Filter to list routes of type {@link FrontEndRouteType#STANDALONE}.
     */
    public static final Specification<FrontEndRoute> IS_STANDALONE = (root, query, builder) -> builder.equal(root.get(FrontEndRoute_.type), FrontEndRouteType.STANDALONE);

    /**
     * Filter to list routes of type {@link FrontEndRouteType#FOOTER_MENU}.
     */
    public static final Specification<FrontEndRoute> IS_FOOTER = (root, query, builder) -> builder.equal(root.get(FrontEndRoute_.type), FrontEndRouteType.FOOTER_MENU);

    /**
     * Filter to list routes of type {@link FrontEndRouteType#ENTRY_ROUTE_MASK} and {@link FrontEndRouteType#CATEGORY_ROUTE_MASK}.
     */
    public static final Specification<FrontEndRoute> IS_MASK = (root, query, builder) -> builder.or(
            builder.equal(root.get(FrontEndRoute_.type), FrontEndRouteType.CATEGORY_ROUTE_MASK),
            builder.equal(root.get(FrontEndRoute_.type), FrontEndRouteType.ENTRY_ROUTE_MASK));
}
