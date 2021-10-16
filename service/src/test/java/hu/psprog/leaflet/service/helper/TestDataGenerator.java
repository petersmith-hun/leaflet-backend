package hu.psprog.leaflet.service.helper;

import java.io.Serializable;

/**
 * Generic test data generator.
 *
 * @param <T> type of entity to generate
 * @author Peter Smith
 */
public interface TestDataGenerator<T extends Serializable> {

    /**
     * Generates one instance of specified type.
     *
     * @return a new instance of given type
     */
    T generate();
}
