package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * FrontEndRoute repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface FrontEndRouteRepository extends SelfStatusAwareJpaRepository<FrontEndRoute, Long>, JpaSpecificationExecutor<FrontEndRoute> {
}
