package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Category repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
