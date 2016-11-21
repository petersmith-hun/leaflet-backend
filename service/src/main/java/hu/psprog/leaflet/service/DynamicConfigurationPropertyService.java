package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.exception.ServiceException;

import java.util.Map;

/**
 * Dynamic Configuration Property service operations interface.
 *
 * @author Peter Smith
 */
public interface DynamicConfigurationPropertyService {

    /**
     * Returns all existing configuration value as Map.
     *
     * @return map of existing DCP key-value pairs
     */
    Map<String, String> getAll();

    /**
     * Returns value of property under given key.
     *
     * @param key key of the property
     * @return value stored under given key or {@code null} if not existing
     */
    String get(String key);

    /**
     * Creates a new property.
     *
     * @param key key of the property
     * @param value value of the property
     */
    void add(String key, String value) throws ServiceException;

    /**
     * Creates new properties.
     *
     * @param properties map of new properties.
     */
    void bulkAdd(Map<String, String> properties) throws ServiceException;

    /**
     * Updates an existing property.
     *
     * @param key key of the property
     * @param value value of the property
     */
    void update(String key, String value) throws ServiceException;

    /**
     * Removes an existing property.
     *
     * @param key key of the property.
     */
    void delete(String key) throws ServiceException;
}
