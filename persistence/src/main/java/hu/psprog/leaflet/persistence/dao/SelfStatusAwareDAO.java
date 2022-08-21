package hu.psprog.leaflet.persistence.dao;

import java.io.Serializable;

/**
 * DAO for repositories that can change the enabled/disabled status of their entities.
 *
 * @param <ID> entity identifier of type ID
 * @author Peter Smith
 */
public interface SelfStatusAwareDAO<ID extends Serializable>  {

    void enable(ID id);

    void disable(ID id);
}
