package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.DynamicConfigurationPropertyDAO;
import hu.psprog.leaflet.persistence.entity.DynamicConfigurationProperty;
import hu.psprog.leaflet.persistence.repository.DynamicConfigurationPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link DynamicConfigurationPropertyDAO}
 *
 * @author Peter Smith
 */
@Component
public class DynamicConfigurationPropertyDAOImpl extends BaseDAOImpl<DynamicConfigurationProperty, String> implements DynamicConfigurationPropertyDAO {

    @Autowired
    public DynamicConfigurationPropertyDAOImpl(final DynamicConfigurationPropertyRepository dynamicConfigurationPropertyRepository, JpaContext jpaContext) {
        super(dynamicConfigurationPropertyRepository, jpaContext);
    }

    @Override
    protected void doUpdate(DynamicConfigurationProperty currentEntity, DynamicConfigurationProperty updatedEntity) {
        currentEntity.setValue(updatedEntity.getValue());
    }
}
