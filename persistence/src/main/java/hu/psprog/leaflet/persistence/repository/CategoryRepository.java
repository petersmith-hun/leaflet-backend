package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Category repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface CategoryRepository extends SelfStatusAwareJpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
}
