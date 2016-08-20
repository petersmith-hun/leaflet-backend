package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * Enabling/disabling self status aware entities.
 *
 * @param <ID> type of id field
 * @author Peter Smith
 */
@NoRepositoryBean
public interface SelfStatusAwareJpaRepository<T extends SerializableEntity, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Enables given entity.
     *
     * @param id identifier of entity
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.enabled = true WHERE e.id = :id")
    public void enable(@Param("id") ID id);

    /**
     * Disables given entity.
     *
     * @param id identifier of entity
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.enabled = false WHERE e.id = :id")
    public void disable(@Param("id") ID id);
}
