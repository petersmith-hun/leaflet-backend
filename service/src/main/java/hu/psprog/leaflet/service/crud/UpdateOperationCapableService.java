package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.BaseVO;

import java.io.Serializable;

/**
 * Interface for services having UPDATE operations.
 *
 * @param <T> {@link BaseVO} type of INPUT entity
 * @param <R> {@link BaseVO} type of RETURNING entity
 * @param <ID> {@link Serializable} identifier for entity
 * @author Peter Smith
 */
public interface UpdateOperationCapableService<T extends BaseVO, R extends BaseVO, ID extends Serializable> {

    /**
     * Updates entity of type T specified by given ID. Returns updated entity of type R.
     *
     * @param id {@link Serializable} identifier of entity
     * @param updatedEntity {@link BaseVO} filled with data to updateOne to
     * @return updated entity
     */
    R updateOne(ID id, T updatedEntity) throws ServiceException;
}
