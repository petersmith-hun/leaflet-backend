package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.BaseVO;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for services having READ operations.
 *
 * @param <T> {@link BaseVO} type of entity
 * @param <ID> {@link Serializable} identifier for entity
 * @author Peter Smith
 */
public interface ReadOperationCapableService<T extends BaseVO, ID extends Serializable> {

    /**
     * Retrieves entity of type T specified by ID id.
     *
     * @param id {@link Serializable} identifier of entity
     * @return entity identified by given identifier
     */
    T getOne(ID id) throws ServiceException;

    /**
     * Retrieves all entity of type T.
     *
     * @return list of all entities of type T
     */
    List<T> getAll();
}
