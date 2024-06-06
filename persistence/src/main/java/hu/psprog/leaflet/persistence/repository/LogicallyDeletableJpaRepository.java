package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.LogicallyDeletableSelfStatusAwareIdentifiableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Repository providing logical deletion operation on entities.
 *
 * @author Peter Smith
 */
@NoRepositoryBean
public interface LogicallyDeletableJpaRepository<T extends LogicallyDeletableSelfStatusAwareIdentifiableEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Marks entity identified by id as deleted by setting "is_deleted" field to true.
     *
     * @param id identifier of entity
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.deleted = true, e.lastModified = CURRENT_TIMESTAMP WHERE e.id = :id")
    void markAsDeleted(@Param("id") ID id);

    /**
     * Reverts logical deletion of entity identified by id by setting "is_deleted" field to false.
     *
     * @param id identifier of entity
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.deleted = false, e.lastModified = CURRENT_TIMESTAMP WHERE e.id = :id")
    void revertLogicalDeletion(@Param("id") ID id);
}
