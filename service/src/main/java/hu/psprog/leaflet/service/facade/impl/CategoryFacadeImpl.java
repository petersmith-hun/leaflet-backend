package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.CategoryService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CategoryFacade;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link CategoryFacade}.
 *
 * @author Peter Smith
 */
@Service
public class CategoryFacadeImpl implements CategoryFacade {

    private final CategoryService categoryService;

    @Autowired
    public CategoryFacadeImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryVO createOne(CategoryVO entity) throws ServiceException {
        Long categoryID = categoryService.createOne(entity);
        return categoryService.getOne(categoryID);
    }

    @Override
    public CategoryVO getOne(Long id) throws ServiceException {
        return categoryService.getOne(id);
    }

    @Override
    public List<CategoryVO> getAll() {
        return categoryService.getAll();
    }

    @Override
    public List<CategoryVO> getAllPublic() {
        return categoryService.getAllPublic();
    }

    @Override
    public CategoryVO updateOne(Long id, CategoryVO updatedCategory) throws ServiceException {
        categoryService.updateOne(id, updatedCategory);
        return categoryService.getOne(id);
    }

    @Override
    public void deletePermanently(Long id) throws ServiceException {
        categoryService.deleteByID(id);
    }

    @Override
    public CategoryVO changeStatus(Long id) throws ServiceException {

        CategoryVO categoryVO = categoryService.getOne(id);
        if (categoryVO.isEnabled()) {
            categoryService.disable(id);
        } else {
            categoryService.enable(id);
        }

        return categoryService.getOne(id);
    }
}
