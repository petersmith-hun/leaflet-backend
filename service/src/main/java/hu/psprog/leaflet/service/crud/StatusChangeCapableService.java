package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.exception.EntityNotFoundException;

import java.io.Serializable;

/**
 * Interface for services that are capable of changing an entity's status.
 *
 * @author Peter Smith
 */
public interface StatusChangeCapableService<ID extends Serializable> {

    /**
     * Enables given entity.
     *
     * @param id ID of the entity
     * @throws EntityNotFoundException if given entity is not found
     */
    void enable(ID id) throws EntityNotFoundException;

    /**
     * Disables given entity.
     *
     * @param id ID of the entity
     * @throws EntityNotFoundException if given entity is not found
     */
    void disable(ID id) throws EntityNotFoundException;
}
