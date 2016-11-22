package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.DynamicConfigurationPropertyDAO;
import hu.psprog.leaflet.persistence.entity.DynamicConfigurationProperty;
import hu.psprog.leaflet.persistence.repository.DynamicConfigurationPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link DynamicConfigurationPropertyDAO}
 *
 * @author Peter Smith
 */
@Component
public class DynamicConfigurationPropertyDAOImpl extends BaseDAOImpl<DynamicConfigurationProperty, String> implements DynamicConfigurationPropertyDAO {

    @Autowired
    public DynamicConfigurationPropertyDAOImpl(final DynamicConfigurationPropertyRepository dynamicConfigurationPropertyRepository) {
        super(dynamicConfigurationPropertyRepository);
    }

    @Transactional
    @Override
    public DynamicConfigurationProperty updateOne(String key, DynamicConfigurationProperty updatedEntity) {

        DynamicConfigurationProperty currentDCP = jpaRepository.getOne(key);
        if (currentDCP != null) {
            currentDCP.setValue(updatedEntity.getValue());
            jpaRepository.flush();
        }
        return currentDCP;
    }
}
