package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.CategoryService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;

import java.util.List;

/**
 * Facade for {@link CategoryService}.
 * 
 * @author Peter Smith
 */
public interface CategoryFacade {

    /**
     * Passes category for persistence layer and returns ID of newly created category.
     *
     * @param entity {@link CategoryVO} value object
     * @return created category data
     */
    CategoryVO createOne(CategoryVO entity) throws ServiceException;

    /**
     * Retrieves entity of type {@link CategoryVO} specified by ID.
     *
     * @param id ID of the category
     * @return category identified by given identifier
     */
    CategoryVO getOne(Long id) throws ServiceException;

    /**
     * Retrieves all entity of type {@link CategoryVO}.
     *
     * @return list of all entities of type {@link CategoryVO}
     */
    List<CategoryVO> getAll();

    /**
     * Retrieves all public entity of type {@link CategoryVO}.
     *
     * @return list of public entities of type {@link CategoryVO}
     */
    List<CategoryVO> getAllPublic();

    /**
     * Returns number of categories.
     *
     * @return number of categories
     */
    Long count();

    /**
     * Updates category specified by given ID. Returns updated category.
     *
     * @param id ID of category
     * @param updatedCategory updated {@link CategoryVO}
     * @return updated category
     */
    CategoryVO updateOne(Long id, CategoryVO updatedCategory) throws ServiceException;

    /**
     * Deletes category by its identifier.
     *
     * @param id ID of the category to delete
     */
    void deletePermanently(Long id) throws ServiceException;

    /**
     * Changes category status.
     * If category is currently enabled, status will be updated to disabled and backwards.
     *
     * @param id ID of category to change status of
     * @return updated category data
     * @throws ServiceException if category cannot be found or status change cannot be performed
     */
    CategoryVO changeStatus(Long id) throws ServiceException;
}
