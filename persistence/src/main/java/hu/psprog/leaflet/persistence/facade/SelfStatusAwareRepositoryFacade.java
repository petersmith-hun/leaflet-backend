package hu.psprog.leaflet.persistence.facade;

import java.io.Serializable;

/**
 * Facade for repositories that can change the enabled/disabled status of their entities.
 *
 * @param <ID> entity identifier of type ID
 * @author Peter Smith
 */
public interface SelfStatusAwareRepositoryFacade<ID extends Serializable>  {

    public void enable(ID id);

    public void disable(ID id);
}
