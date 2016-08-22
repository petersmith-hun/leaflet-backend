package hu.psprog.leaflet.persistence.dao;

import java.io.Serializable;

/**
 * DAO for repositories that can change the enabled/disabled status of their entities.
 *
 * @param <ID> entity identifier of type ID
 * @author Peter Smith
 */
public interface SelfStatusAwareDAO<ID extends Serializable>  {

    public void enable(ID id);

    public void disable(ID id);
}
