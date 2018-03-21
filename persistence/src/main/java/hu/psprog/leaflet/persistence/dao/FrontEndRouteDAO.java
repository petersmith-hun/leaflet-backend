package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.repository.FrontEndRouteRepository;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * DAO interface for {@link FrontEndRouteRepository}.
 *
 * @author Peter Smith
 */
public interface FrontEndRouteDAO extends BaseDAO<FrontEndRoute, Long>, SelfStatusAwareDAO<Long> {

    List<FrontEndRoute> findAll(Specification<FrontEndRoute> specification);
}
