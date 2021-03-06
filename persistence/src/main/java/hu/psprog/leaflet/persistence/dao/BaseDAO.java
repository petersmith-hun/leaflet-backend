package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * Base DAO for repositories.
 *
 * @param <T> base entity of type T
 * @param <ID> entity identifier of type ID
 * @author Peter Smith
 */
public interface BaseDAO<T extends SerializableEntity, ID extends Serializable> {

    public boolean exists(ID id);

    public T findOne(ID id);

    public List<T> findAll();

    public Page<T> findAll(Pageable pageable);

    public T updateOne(ID id, T updatedEntity);

    public void delete(ID id);

    public <S extends T> S save(S entity);

    public long count();
}
