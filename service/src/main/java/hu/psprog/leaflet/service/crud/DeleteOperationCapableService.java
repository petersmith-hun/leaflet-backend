package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.vo.BaseVO;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for services having DELETE operations.
 *
 * @param <T> {@link BaseVO} type of entity
 * @param <ID> {@link Serializable} identifier for entity
 * @author Peter Smith
 */
public interface DeleteOperationCapableService<T extends BaseVO, ID extends Serializable> {

    /**
     * Deletes entity of type T by entity object.
     *
     * @param entity {@link BaseVO} object to delete
     */
    public void deleteByEntity(T entity);

    /**
     * Deletes entity of type T by its identifier.
     *
     * @param id {@link Serializable} identifier of entity
     */
    public void deleteByID(ID id);

    /**
     * Deletes a list of entities of type T by their identifier.
     *
     * @param ids {@link List} of {@link Serializable} identifiers
     */
    public void deleteBulkByIDs(List<ID> ids);
}
