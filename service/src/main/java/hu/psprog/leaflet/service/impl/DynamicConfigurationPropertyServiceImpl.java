package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DynamicConfigurationPropertyDAO;
import hu.psprog.leaflet.persistence.entity.DynamicConfigurationProperty;
import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DynamicConfigurationPropertyService}.
 *
 * @author Peter Smith
 */
@Service
public class DynamicConfigurationPropertyServiceImpl implements DynamicConfigurationPropertyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicConfigurationPropertyServiceImpl.class);

    @Autowired
    private DynamicConfigurationPropertyDAO dynamicConfigurationPropertyDAO;

    private Map<String, String> dcpStore;

    @PostConstruct
    public void setup() {
        LOGGER.info("Populating DCP Store...");
        dcpStore = dynamicConfigurationPropertyDAO.findAll().stream()
                .collect(Collectors.toMap(DynamicConfigurationProperty::getKey, DynamicConfigurationProperty::getValue));
        LOGGER.info("Loaded {} properties into DCP Store.", dcpStore.size());
    }

    @Override
    public Map<String, String> getAll() {
        return dcpStore;
    }

    @Override
    public String get(String key) {
        return dcpStore.get(key);
    }

    @Override
    public void add(String key, String value) throws ServiceException {

        try {
            DynamicConfigurationProperty dynamicConfigurationProperty = new DynamicConfigurationProperty(key, value);
            dynamicConfigurationPropertyDAO.save(dynamicConfigurationProperty);
            dcpStore.put(key, value);
        } catch (PersistenceException e) {
            LOGGER.error("An exception occurred while storing DCP entry.", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void bulkAdd(Map<String, String> properties) throws ServiceException {

        for(Map.Entry<String, String> entry : properties.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void update(String key, String value) throws ServiceException {

        try {
            DynamicConfigurationProperty dynamicConfigurationProperty = new DynamicConfigurationProperty(key, value);
            dynamicConfigurationPropertyDAO.updateOne(key, dynamicConfigurationProperty);
            dcpStore.put(key, value);
        } catch (PersistenceException e) {
            LOGGER.error("An exception occurred while updating DCP entry.", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(String key) throws ServiceException {

        try {
            dynamicConfigurationPropertyDAO.delete(key);
            dcpStore.remove(key);
        } catch (PersistenceException e) {
            LOGGER.error("An exception occurred while removing DCP entry.", e);
            throw new ServiceException(e);
        }
    }
}
