package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.FrontEndRouteDAO;
import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.repository.FrontEndRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link FrontEndRouteDAO}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteDAOImpl extends SelfStatusAwareDAOImpl<FrontEndRoute, Long> implements FrontEndRouteDAO {

    @Autowired
    public FrontEndRouteDAOImpl(FrontEndRouteRepository frontEndRouteRepository) {
        super(frontEndRouteRepository);
    }

    @Override
    public List<FrontEndRoute> findAll(Specification<FrontEndRoute> specification) {
        return ((FrontEndRouteRepository) jpaRepository).findAll(specification);
    }

    @Override
    public FrontEndRoute updateOne(Long id, FrontEndRoute updatedEntity) {

        FrontEndRoute entity = jpaRepository.getOne(id);
        if (Objects.nonNull(entity)) {
            entity.setRouteId(updatedEntity.getRouteId());
            entity.setName(updatedEntity.getName());
            entity.setSequenceNumber(updatedEntity.getSequenceNumber());
            entity.setType(updatedEntity.getType());
            entity.setEnabled(updatedEntity.isEnabled());
            entity.setLastModified(new Date());
            jpaRepository.flush();
        }

        return entity;
    }
}
