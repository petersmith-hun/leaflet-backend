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
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CategoryService}.
 *
 * @author Peter Smith
 */
@Service
public class CategoryServiceImpl implements CategoryService {

     private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryDAO categoryDAO;
    private final CategoryToCategoryVOConverter categoryToCategoryVOConverter;
    private final CategoryVOToCategoryConverter categoryVOToCategoryConverter;

    @Autowired
    public CategoryServiceImpl(CategoryDAO categoryDAO, CategoryToCategoryVOConverter categoryToCategoryVOConverter,
                               CategoryVOToCategoryConverter categoryVOToCategoryConverter) {
        this.categoryDAO = categoryDAO;
        this.categoryToCategoryVOConverter = categoryToCategoryVOConverter;
        this.categoryVOToCategoryConverter = categoryVOToCategoryConverter;
    }

    @Override
    @PermitScope.Read.Categories
    public CategoryVO getOne(Long id) throws ServiceException {

        Category category = categoryDAO.findOne(id);

        if (category == null) {
            throw new EntityNotFoundException(Category.class, id);
        }

        return categoryToCategoryVOConverter.convert(category);
    }

    @Override
    @PermitScope.Read.Categories
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
    @PermitScope.Read.Categories
    public Long count() {

        return categoryDAO.count();
    }

    @Override
    @PermitScope.Write.Categories
    public Long createOne(CategoryVO entity) throws ServiceException {

        Category category = categoryVOToCategoryConverter.convert(entity);
        Category savedCategory = categoryDAO.save(category);

        if (savedCategory == null) {
            throw new EntityCreationException(Category.class);
        }

        LOGGER.info("New category [{}] has been created with ID [{}]", entity.getTitle(), savedCategory.getId());

        return savedCategory.getId();
    }

    @Override
    @PermitScope.Write.Categories
    public CategoryVO updateOne(Long id, CategoryVO updatedEntity) throws ServiceException {

        Category updatedCategory = categoryDAO.updateOne(id, categoryVOToCategoryConverter.convert(updatedEntity));

        if (updatedCategory == null) {
            throw new EntityNotFoundException(Category.class, id);
        }

        LOGGER.info("Existing category [{}] with ID [{}] has been updated", updatedCategory.getTitle(), id);

        return categoryToCategoryVOConverter.convert(updatedCategory);
    }

    @Override
    @PermitScope.Write.Categories
    public void deleteByID(Long id) throws ServiceException {

        if (!categoryDAO.exists(id)) {
            throw new EntityNotFoundException(Category.class, id);
        }

        categoryDAO.delete(id);
        LOGGER.info("Deleted category of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Categories
    public void enable(Long id) throws EntityNotFoundException {

        if (!categoryDAO.exists(id)) {
            throw new EntityNotFoundException(Category.class, id);
        }

        categoryDAO.enable(id);
        LOGGER.info("Enabled category of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Categories
    public void disable(Long id) throws EntityNotFoundException {

        if (!categoryDAO.exists(id)) {
            throw new EntityNotFoundException(Category.class, id);
        }

        categoryDAO.disable(id);
        LOGGER.info("Disabled category of ID [{}]", id);
    }
}
