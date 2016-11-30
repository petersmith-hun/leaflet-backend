package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Category;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.CategoryRepository}.
 *
 * @author Peter Smith
 */
public interface CategoryDAO extends BaseDAO<Category, Long>, SelfStatusAwareDAO<Long> {

    public List<Category> findAll(Specification<Category> specification);
}
