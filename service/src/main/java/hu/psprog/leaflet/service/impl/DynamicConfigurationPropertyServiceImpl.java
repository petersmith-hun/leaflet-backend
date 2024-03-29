package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DynamicConfigurationPropertyDAO;
import hu.psprog.leaflet.persistence.entity.DynamicConfigurationProperty;
import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.PersistenceException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DynamicConfigurationPropertyService}.
 *
 * @author Peter Smith
 */
@Service
public class DynamicConfigurationPropertyServiceImpl implements DynamicConfigurationPropertyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicConfigurationPropertyServiceImpl.class);

    private final DynamicConfigurationPropertyDAO dynamicConfigurationPropertyDAO;
    private Map<String, String> dcpStore;

    @Autowired
    public DynamicConfigurationPropertyServiceImpl(DynamicConfigurationPropertyDAO dynamicConfigurationPropertyDAO) {
        this.dynamicConfigurationPropertyDAO = dynamicConfigurationPropertyDAO;
    }

    @PostConstruct
    public void populateStore() {
        LOGGER.info("Populating DCP Store...");
        dcpStore = dynamicConfigurationPropertyDAO.findAll().stream()
                .collect(Collectors.toMap(DynamicConfigurationProperty::getKey, DynamicConfigurationProperty::getValue));
        LOGGER.info("Loaded {} properties into DCP Store.", dcpStore.size());
    }

    @Override
    @PermitScope.Read.Admin
    public Map<String, String> getAll() {
        return dcpStore;
    }

    @Override
    public String get(String key) {
        String value = dcpStore.get(key);
        if (Objects.isNull(value)) {
            LOGGER.warn("No value found for requested key '{}'.", key);
        }

        return value;
    }

    @Override
    @PermitScope.Write.Admin
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
    @PermitScope.Write.Admin
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
    @PermitScope.Write.Admin
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
