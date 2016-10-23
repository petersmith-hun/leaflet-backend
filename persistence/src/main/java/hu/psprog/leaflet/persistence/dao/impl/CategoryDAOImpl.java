package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.CategoryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * DAO implementation for {@link CategoryRepository}.
 *
 * @author Peter Smith
 */
@Component
public class CategoryDAOImpl extends SelfStatusAwareDAOImpl<Category, Long> implements CategoryDAO {

    @Autowired
    public CategoryDAOImpl(final CategoryRepository categoryRepository) {
        super(categoryRepository);
    }

    @Transactional
    @Override
    public Category updateOne(Long id, Category updatedEntity) {

        Category currentCategory = jpaRepository.getOne(id);
        if (currentCategory != null) {
            currentCategory.setTitle(updatedEntity.getTitle());
            currentCategory.setDescription(updatedEntity.getDescription());
            currentCategory.setLastModified(new Date());
            jpaRepository.flush();
        }

        return currentCategory;
    }
}
