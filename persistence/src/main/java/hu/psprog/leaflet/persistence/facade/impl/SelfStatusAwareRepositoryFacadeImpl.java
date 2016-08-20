package hu.psprog.leaflet.persistence.facade.impl;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import hu.psprog.leaflet.persistence.facade.SelfStatusAwareRepositoryFacade;
import hu.psprog.leaflet.persistence.repository.SelfStatusAwareJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author Peter Smith
 */
public abstract class SelfStatusAwareRepositoryFacadeImpl<T extends SerializableEntity, ID extends Serializable>
        extends BaseRepositoryFacadeImpl<T, ID> implements SelfStatusAwareRepositoryFacade<ID> {

    public SelfStatusAwareRepositoryFacadeImpl(JpaRepository<T, ID> jpaRepository) {
        super(jpaRepository);
    }

    @Override
    public void enable(ID id) {
        ((SelfStatusAwareJpaRepository) jpaRepository).enable(id);
    }

    @Override
    public void disable(ID id) {
        ((SelfStatusAwareJpaRepository) jpaRepository).disable(id);
    }
}
