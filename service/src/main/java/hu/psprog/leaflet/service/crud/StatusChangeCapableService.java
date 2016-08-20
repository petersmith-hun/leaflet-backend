package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.exception.EntityNotFoundException;

import java.io.Serializable;

/**
 * @author Peter Smith
 */
public interface StatusChangeCapableService<ID extends Serializable> {

    public void enable(ID id) throws EntityNotFoundException;

    public void disable(ID id) throws EntityNotFoundException;
}
