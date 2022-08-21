package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base DAO for repositories.
 *
 * @param <T> base entity of type T
 * @param <ID> entity identifier of type ID
 * @author Peter Smith
 */
public interface BaseDAO<T extends SerializableEntity, ID extends Serializable> {

    boolean exists(ID id);

    T findOne(ID id);
    
    Optional<T> findById(ID id);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    T updateOne(ID id, T updatedEntity);

    void delete(ID id);

    <S extends T> S save(S entity);

    long count();
}
