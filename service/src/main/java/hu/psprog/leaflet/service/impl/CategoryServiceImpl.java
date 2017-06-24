package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CategoryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.repository.specification.CategorySpecification;
import hu.psprog.leaflet.service.CategoryService;
import hu.psprog.leaflet.service.converter.CategoryToCategoryVOConverter;
import hu.psprog.leaflet.service.converter.CategoryVOToCategoryConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CategoryService}.
 *
 * @author Peter Smith
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryDAO categoryDAO;
    private CategoryToCategoryVOConverter categoryToCategoryVOConverter;
    private CategoryVOToCategoryConverter categoryVOToCategoryConverter;

    @Autowired
    public CategoryServiceImpl(CategoryDAO categoryDAO, CategoryToCategoryVOConverter categoryToCategoryVOConverter,
                               CategoryVOToCategoryConverter categoryVOToCategoryConverter) {
        this.categoryDAO = categoryDAO;
        this.categoryToCategoryVOConverter = categoryToCategoryVOConverter;
        this.categoryVOToCategoryConverter = categoryVOToCategoryConverter;
    }

    @Override
    public CategoryVO getOne(Long id) throws ServiceException {

        Category category = categoryDAO.findOne(id);

        if (category == null) {
            throw new EntityNotFoundException(Category.class, id);
        }

        return categoryToCategoryVOConverter.convert(category);
    }

    @Override
    public List<CategoryVO> getAll() {

        return categoryDAO.findAll().stream()
                .map(e -> categoryToCategoryVOConverter.convert(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryVO> getAllPublic() {

        return categoryDAO.findAll(CategorySpecification.isEnabled).stream()
                .map(e -> categoryToCategoryVOConverter.convert(e))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {

        return categoryDAO.count();
    }

    @Override
    public Long createOne(CategoryVO entity) throws ServiceException {

        Category category = categoryVOToCategoryConverter.convert(entity);
        Category savedCategory = categoryDAO.save(category);

        if (savedCategory == null) {
            throw new EntityCreationException(Category.class);
        }

        return savedCategory.getId();
    }

    @Override
    public List<Long> createBulk(List<CategoryVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (CategoryVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    public CategoryVO updateOne(Long id, CategoryVO updatedEntity) throws ServiceException {

        Category updatedCategory = categoryDAO.updateOne(id, categoryVOToCategoryConverter.convert(updatedEntity));

        if (updatedCategory == null) {
            throw new EntityNotFoundException(Category.class, id);
        }

        return categoryToCategoryVOConverter.convert(updatedCategory);
    }

    @Override
    public List<CategoryVO> updateBulk(Map<Long, CategoryVO> updatedEntities) throws ServiceException {

        List<CategoryVO> entryVOs = new LinkedList<>();

        Iterator<Map.Entry<Long, CategoryVO>> entities = updatedEntities.entrySet().iterator();
        while (entities.hasNext()) {
            Map.Entry<Long, CategoryVO> currentEntity = entities.next();
            CategoryVO updatedEntity = updateOne(currentEntity.getKey(), currentEntity.getValue());
            entryVOs.add(updatedEntity);
        }

        return entryVOs;
    }

    @Override
    public void deleteByEntity(CategoryVO entity) throws ServiceException {

        if (!categoryDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Category.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {

        try {
            categoryDAO.delete(id);
        } catch (IllegalArgumentException exc){
            throw new EntityNotFoundException(Category.class, id);
        }
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {

        if (!categoryDAO.exists(id)) {
            throw new EntityNotFoundException(Category.class, id);
        }

        categoryDAO.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!categoryDAO.exists(id)) {
            throw new EntityNotFoundException(Category.class, id);
        }

        categoryDAO.disable(id);
    }
}
