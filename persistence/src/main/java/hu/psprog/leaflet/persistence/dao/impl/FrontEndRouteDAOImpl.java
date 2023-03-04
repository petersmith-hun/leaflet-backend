package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.FrontEndRouteDAO;
import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.repository.FrontEndRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of {@link FrontEndRouteDAO}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteDAOImpl extends SelfStatusAwareDAOImpl<FrontEndRoute, Long> implements FrontEndRouteDAO {

    @Autowired
    public FrontEndRouteDAOImpl(FrontEndRouteRepository frontEndRouteRepository, JpaContext jpaContext) {
        super(frontEndRouteRepository, jpaContext);
    }

    @Override
    public List<FrontEndRoute> findAll(Specification<FrontEndRoute> specification) {
        return ((FrontEndRouteRepository) jpaRepository).findAll(specification);
    }

    @Override
    protected void doUpdate(FrontEndRoute currentEntity, FrontEndRoute updatedEntity) {
        
        currentEntity.setRouteId(updatedEntity.getRouteId());
        currentEntity.setName(updatedEntity.getName());
        currentEntity.setUrl(updatedEntity.getUrl());
        currentEntity.setSequenceNumber(updatedEntity.getSequenceNumber());
        currentEntity.setType(updatedEntity.getType());
        currentEntity.setAuthRequirement(updatedEntity.getAuthRequirement());
        currentEntity.setEnabled(updatedEntity.isEnabled());
    }
}
