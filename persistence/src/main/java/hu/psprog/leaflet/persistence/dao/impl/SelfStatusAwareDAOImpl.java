package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.SelfStatusAwareDAO;
import hu.psprog.leaflet.persistence.entity.SelfStatusAwareIdentifiableEntity;
import hu.psprog.leaflet.persistence.repository.SelfStatusAwareJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * Default implementation of {@link SelfStatusAwareDAO}.
 *
 * @author Peter Smith
 */
public abstract class SelfStatusAwareDAOImpl<T extends SelfStatusAwareIdentifiableEntity<ID>, ID extends Serializable>
        extends BaseDAOImpl<T, ID> implements SelfStatusAwareDAO<ID> {

    public SelfStatusAwareDAOImpl(JpaRepository<T, ID> jpaRepository) {
        super(jpaRepository);
    }

    @Override
    public void enable(ID id) {
        ((SelfStatusAwareJpaRepository<T, ID>) jpaRepository).enable(id);
    }

    @Override
    public void disable(ID id) {
        ((SelfStatusAwareJpaRepository<T, ID>) jpaRepository).disable(id);
    }
}
