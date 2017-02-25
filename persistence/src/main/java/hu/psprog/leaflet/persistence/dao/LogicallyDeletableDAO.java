package hu.psprog.leaflet.persistence.dao;

import java.io.Serializable;

/**
 * @author Peter Smith
 */
public interface LogicallyDeletableDAO<ID extends Serializable> {

    void markAsDeleted(ID id);

    void revertLogicalDeletion(ID id);
}
