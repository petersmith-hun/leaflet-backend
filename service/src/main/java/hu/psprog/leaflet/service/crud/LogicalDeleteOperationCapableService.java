package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.LogicallyDeletableSelfStatusAwareIdentifiableVO;

import java.io.Serializable;

/**
 * Interface for services having logical deletion operations.
 *
 * @author Peter Smith
 */
public interface LogicalDeleteOperationCapableService<T extends LogicallyDeletableSelfStatusAwareIdentifiableVO, ID extends Serializable>
        extends DeleteOperationCapableService<T, ID> {

    /**
     * Logically deletes given entity.
     *
     * @param entity entity to delete logically
     */
    void deleteLogicallyByEntity(T entity) throws ServiceException;

    /**
     * Restores entity by reverting logical deletion.
     *
     * @param entity entity to restore
     */
    void restoreEntity(T entity) throws ServiceException;
}
