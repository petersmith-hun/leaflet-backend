package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.CategoryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DAO implementation for {@link CategoryRepository}.
 *
 * @author Peter Smith
 */
@Component
public class CategoryDAOImpl extends SelfStatusAwareDAOImpl<Category, Long> implements CategoryDAO {

    @Autowired
    public CategoryDAOImpl(final CategoryRepository categoryRepository, JpaContext jpaContext) {
        super(categoryRepository, jpaContext);
    }

    @Override
    public List<Category> findAll(Specification<Category> specification) {
        return ((CategoryRepository) jpaRepository).findAll(specification);
    }

    @Override
    protected void doUpdate(Category currentEntity, Category updatedEntity) {

        currentEntity.setTitle(updatedEntity.getTitle());
        currentEntity.setDescription(updatedEntity.getDescription());
    }
}
