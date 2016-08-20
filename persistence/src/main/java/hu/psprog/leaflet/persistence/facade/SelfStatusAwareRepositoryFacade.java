package hu.psprog.leaflet.persistence.facade;

import java.io.Serializable;

/**
 * @author Peter Smith
 */
public interface SelfStatusAwareRepositoryFacade<ID extends Serializable>  {

    public void enable(ID id);

    public void disable(ID id);
}
