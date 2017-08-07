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
import hu.psprog.leaflet.service.security.annotation.PermitEditorOrAdmin;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

     private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

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
    @PermitEditorOrAdmin
    public CategoryVO getOne(Long id) throws ServiceException {

        Category category = categoryDAO.findOne(id);

        if (category == null) {
            throw new EntityNotFoundException(Category.class, id);
        }

        return categoryToCategoryVOConverter.convert(category);
    }

    @Override
    @PermitEditorOrAdmin
    public List<CategoryVO> getAll() {

        return categoryDAO.findAll().stream()
                .map(categoryToCategoryVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryVO> getAllPublic() {

        return categoryDAO.findAll(CategorySpecification.IS_ENABLED).stream()
                .map(categoryToCategoryVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitEditorOrAdmin
    public Long count() {

        return categoryDAO.count();
    }

    @Override
    @PermitEditorOrAdmin
    public Long createOne(CategoryVO entity) throws ServiceException {

        Category category = categoryVOToCategoryConverter.convert(entity);
        Category savedCategory = categoryDAO.save(category);

        if (savedCategory == null) {
            throw new EntityCreationException(Category.class);
        }

        return savedCategory.getId();
    }

    @Override
    @PermitEditorOrAdmin
    public List<Long> createBulk(List<CategoryVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (CategoryVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    @PermitEditorOrAdmin
    public CategoryVO updateOne(Long id, CategoryVO updatedEntity) throws ServiceException {

        Category updatedCategory = categoryDAO.updateOne(id, categoryVOToCategoryConverter.convert(updatedEntity));

        if (updatedCategory == null) {
            throw new EntityNotFoundException(Category.class, id);
        }

        return categoryToCategoryVOConverter.convert(updatedCategory);
    }

    @Override
    @PermitEditorOrAdmin
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
    @PermitEditorOrAdmin
    public void deleteByEntity(CategoryVO entity) throws ServiceException {

        if (!categoryDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Category.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    @PermitEditorOrAdmin
    public void deleteByID(Long id) throws ServiceException {

        try {
            categoryDAO.delete(id);
        } catch (IllegalArgumentException exc) {
            LOGGER.error("Error occurred during deletion", exc);
            throw new EntityNotFoundException(Category.class, id);
        }
    }

    @Override
    @PermitEditorOrAdmin
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    @PermitEditorOrAdmin
    public void enable(Long id) throws EntityNotFoundException {

        if (!categoryDAO.exists(id)) {
            throw new EntityNotFoundException(Category.class, id);
        }

        categoryDAO.enable(id);
    }

    @Override
    @PermitEditorOrAdmin
    public void disable(Long id) throws EntityNotFoundException {

        if (!categoryDAO.exists(id)) {
            throw new EntityNotFoundException(Category.class, id);
        }

        categoryDAO.disable(id);
    }
}
