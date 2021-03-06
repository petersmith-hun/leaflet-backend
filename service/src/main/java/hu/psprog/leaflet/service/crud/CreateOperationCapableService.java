package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.BaseVO;

import java.io.Serializable;

/**
 * Interface for services having CREATE operations.
 *
 * @param <T> {@link BaseVO} type of entity
 * @param <ID> {@link Serializable} identifier for entity
 * @author Peter Smith
 */
public interface CreateOperationCapableService<T extends BaseVO, ID extends Serializable> {

    /**
     * Passes entity for persistence layer and returns ID of newly created entity.
     *
     * @param entity {@link BaseVO} value object
     * @return identifier of newly created entity
     */
    ID createOne(T entity) throws ServiceException;
}
