package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.DynamicConfigurationProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for DCP store.
 *
 * @author Peter Smith
 */
@Repository
public interface DynamicConfigurationPropertyRepository extends JpaRepository<DynamicConfigurationProperty, String> {
}
