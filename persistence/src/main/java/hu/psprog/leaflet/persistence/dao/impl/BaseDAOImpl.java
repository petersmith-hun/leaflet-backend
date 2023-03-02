package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.BaseDAO;
import hu.psprog.leaflet.persistence.entity.SelfStatusAwareIdentifiableEntity;
import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link BaseDAO}.
 *
 * @author Peter Smith
 */
public abstract class BaseDAOImpl<T extends SerializableEntity, ID extends Serializable> implements BaseDAO<T, ID> {

    protected JpaRepository<T, ID> jpaRepository;

    private final JpaContext jpaContext;

    public BaseDAOImpl(JpaRepository<T, ID> jpaRepository, JpaContext jpaContext) {
        this.jpaRepository = jpaRepository;
        this.jpaContext = jpaContext;
    }

    @Override
    public boolean exists(ID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<T> findById(ID id) {
        return jpaRepository.findById(id);
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
        jpaRepository.deleteById(id);
    }

    @Override
    public <S extends T> S save(S entity) {

        if (entity instanceof SelfStatusAwareIdentifiableEntity) {
            ((SelfStatusAwareIdentifiableEntity<?>) entity).setCreated(new Date());
        }

        S savedEntity = jpaRepository.saveAndFlush(entity);
        jpaContext.getEntityManagerByManagedType(entity.getClass()).clear();

        return savedEntity;
    }

    @Transactional
    @Override
    public Optional<T> updateOne(ID id, T updatedEntity) {

        return jpaRepository.findById(id)
                .map(currentEntity -> {

                    doUpdate(currentEntity, updatedEntity);
                    if (currentEntity instanceof SelfStatusAwareIdentifiableEntity) {
                        ((SelfStatusAwareIdentifiableEntity<?>) currentEntity).setLastModified(new Date());
                    }

                    jpaRepository.flush();

                    return currentEntity;
                });
    }

    protected abstract void doUpdate(T currentEntity, T updatedEntity);
}
