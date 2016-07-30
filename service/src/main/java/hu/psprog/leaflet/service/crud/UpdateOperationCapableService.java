package hu.psprog.leaflet.service.crud;

import hu.psprog.leaflet.service.vo.BaseVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    public R updateOne(ID id, T updatedEntity);

    /**
     * Updates a list of entities of type T specified by given ID. Returns updated entities of type R.
     *
     * @param updatedEntities {@link Map} of ID-T identifier-entity pairs.
     * @return list of updates entities
     */
    public List<R> updateBulk(Map<ID, T> updatedEntities);
}
