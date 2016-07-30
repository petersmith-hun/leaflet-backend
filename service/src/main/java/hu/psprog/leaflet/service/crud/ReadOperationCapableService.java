package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.exception.EntityNotFoundException;
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
     * @throws EntityNotFoundException when specified entity can not be found
     */
    public T getOne(ID id) throws EntityNotFoundException;

    /**
     * Retrieves all entity of type T.
     *
     * @return list of all entities of type T
     */
    public List<T> getAll();
}
