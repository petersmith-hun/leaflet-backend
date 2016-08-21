package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Category;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.CategoryRepository}.
 *
 * @author Peter Smith
 */
public interface CategoryDAO extends BaseDAO<Category, Long>, SelfStatusAwareDAO<Long> {
}
