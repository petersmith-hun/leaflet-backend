package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.LogicallyDeletableDAO;
import hu.psprog.leaflet.persistence.entity.LogicallyDeletableSelfStatusAwareIdentifiableEntity;
import hu.psprog.leaflet.persistence.repository.LogicallyDeletableJpaRepository;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author Peter Smith
 */
public abstract class LogicallyDeletableSelfStatusAwareDAOImpl<T extends LogicallyDeletableSelfStatusAwareIdentifiableEntity<ID>, ID extends Serializable>
        extends SelfStatusAwareDAOImpl<T, ID> implements LogicallyDeletableDAO<ID> {

    public LogicallyDeletableSelfStatusAwareDAOImpl(JpaRepository<T, ID> jpaRepository, JpaContext jpaContext) {
        super(jpaRepository, jpaContext);
    }

    @Override
    public void markAsDeleted(ID id) {
        ((LogicallyDeletableJpaRepository<T, ID>) jpaRepository).markAsDeleted(id);
    }

    @Override
    public void revertLogicalDeletion(ID id) {
        ((LogicallyDeletableJpaRepository<T, ID>) jpaRepository).revertLogicalDeletion(id);
    }
}
