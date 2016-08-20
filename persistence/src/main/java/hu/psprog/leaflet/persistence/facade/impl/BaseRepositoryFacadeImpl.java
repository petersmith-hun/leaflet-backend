package hu.psprog.leaflet.persistence.facade.impl;

import hu.psprog.leaflet.persistence.entity.SelfStatusAwareIdentifiableEntity;
import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import hu.psprog.leaflet.persistence.facade.BaseRepositoryFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Peter Smith
 */
public abstract class BaseRepositoryFacadeImpl<T extends SerializableEntity, ID extends Serializable> implements BaseRepositoryFacade<T, ID> {

    protected JpaRepository<T, ID> jpaRepository;

    public BaseRepositoryFacadeImpl(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean exists(ID id) {
        return jpaRepository.exists(id);
    }

    @Override
    public T findOne(ID id) {
        return jpaRepository.findOne(id);
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public void delete(ID id) {
        jpaRepository.delete(id);
    }

    @Override
    public <S extends T> S save(S entity) {

        if (entity instanceof SelfStatusAwareIdentifiableEntity) {
            ((SelfStatusAwareIdentifiableEntity) entity).setCreated(new Date());
        }

        return jpaRepository.save(entity);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public abstract T updateOne(ID id, T updatedEntity);
}
