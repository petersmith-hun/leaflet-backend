package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.category.CategoryCreateRequestModel;
import hu.psprog.leaflet.service.CategoryService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.rest.conversion.ValidationErrorMessagesConverter;
import hu.psprog.leaflet.web.rest.conversion.category.CategoryCreateRequestModelToCategoryVOConverter;
import hu.psprog.leaflet.web.rest.conversion.category.CategoryVOToCategoryDataModelEntityConverter;
import hu.psprog.leaflet.web.rest.conversion.category.CategoryVOToCategoryDataModelListConverter;
import hu.psprog.leaflet.web.rest.conversion.category.CategoryVOToExtendedCategoryDataModelEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for blog category related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_CATEGORIES)
public class CategoriesController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesController.class);

    private static final String REQUESTED_CATEGORY_NOT_FOUND = "Requested category not found";
    private static final String THE_CATEGORY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING = "The category you are looking for is not existing.";
    private static final String CATEGORY_COULD_NOT_BE_CREATED = "Category could not be created. See details:";
    private static final String CATEGORY_COULD_NOT_BE_CREATED_TRY_AGAIN = "Category could not be created, please try again later.";

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryVOToCategoryDataModelEntityConverter categoryVOToCategoryDataModelEntityConverter;

    @Autowired
    private CategoryVOToCategoryDataModelListConverter categoryVOToCategoryDataModelListConverter;

    @Autowired
    private CategoryCreateRequestModelToCategoryVOConverter categoryCreateRequestModelToCategoryVOConverter;

    @Autowired
    private CategoryVOToExtendedCategoryDataModelEntityConverter categoryVOToExtendedCategoryDataModelEntityConverter;

    @Autowired
    private ValidationErrorMessagesConverter validationErrorMessagesConverter;

    /**
     * GET /categories
     * Returns list of all existing categories.
     *
     * @return list of categories
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAllCategories() {

        List<CategoryVO> categories = categoryService.getAll();

        return wrap(categoryVOToCategoryDataModelListConverter.convert(categories));
    }

    /**
     * GET /categories/public
     * Returns list of public categories.
     *
     * @return list of public categories
     */
    @RequestMapping(value = PATH_PUBLIC, method = RequestMethod.GET)
    public ModelAndView getPublicCategories() {

        List<CategoryVO> categories = categoryService.getAllPublic();

        return wrap(categoryVOToCategoryDataModelListConverter.convert(categories));
    }

    /**
     * GET /categories/{id}
     * Returns category identified by given ID.
     *
     * @param id ID of an existing category
     * @return category data if requested category exists
     */
    @RequestMapping(value = PATH_PART_ID, method = RequestMethod.GET)
    public ModelAndView getCategory(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            CategoryVO category = categoryService.getOne(id);

            return wrap(categoryVOToExtendedCategoryDataModelEntityConverter.convert(category));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_CATEGORY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_CATEGORY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * POST /categories
     * Creates a new category.
     *
     * @param categoryCreateRequestModel category data
     * @param bindingResult validation results
     * @return created category data
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView createCategory(@RequestBody @Valid CategoryCreateRequestModel categoryCreateRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return wrap(validationErrorMessagesConverter.convert(bindingResult.getAllErrors()));
        } else {
            try {
                Long categoryID = categoryService.createOne(categoryCreateRequestModelToCategoryVOConverter.convert(categoryCreateRequestModel));
                CategoryVO createdCategory = categoryService.getOne(categoryID);

                return wrap(categoryVOToExtendedCategoryDataModelEntityConverter.convert(createdCategory));
            } catch (ServiceException e) {
                LOGGER.error(CATEGORY_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(CATEGORY_COULD_NOT_BE_CREATED_TRY_AGAIN);
            }
        }
    }

    /**
     * PUT /categories/{id}
     * Updates an existing category.
     *
     * @param id ID of an existing category
     * @param categoryCreateRequestModel category data
     * @param bindingResult validation results
     * @return updated category data
     */
    @RequestMapping(value = PATH_PART_ID, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView updateCategory(@PathVariable(PATH_VARIABLE_ID) Long id,
                                       @RequestBody @Valid CategoryCreateRequestModel categoryCreateRequestModel,
                                       BindingResult bindingResult) throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return wrap(validationErrorMessagesConverter.convert(bindingResult.getAllErrors()));
        } else {
            try {
                categoryService.updateOne(id, categoryCreateRequestModelToCategoryVOConverter.convert(categoryCreateRequestModel));
                CategoryVO updatedCategory = categoryService.getOne(id);

                return wrap(categoryVOToExtendedCategoryDataModelEntityConverter.convert(updatedCategory));
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_CATEGORY_NOT_FOUND, e);
                throw new ResourceNotFoundException(THE_CATEGORY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
            }
        }
    }

    /**
     * PUT /categories/{id}/status
     * Changes status of an existing category.
     *
     * @param id ID of an existing category
     * @return updated category data
     */
    @RequestMapping(value = PATH_CHANGE_STATUS, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView changeStatus(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            CategoryVO categoryVO = categoryService.getOne(id);
            if (categoryVO.isEnabled()) {
                categoryService.disable(id);
            } else {
                categoryService.enable(id);
            }
            CategoryVO updatedCategoryVO = categoryService.getOne(id);

            return wrap(categoryVOToExtendedCategoryDataModelEntityConverter.convert(updatedCategoryVO));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_CATEGORY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_CATEGORY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * DELETE /categories/{id}
     * Deletes an existing category.
     *
     * @param id ID of an existing category
     */
    @RequestMapping(value = PATH_PART_ID, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            CategoryVO categoryVO = categoryService.getOne(id);
            categoryService.deleteByEntity(categoryVO);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_CATEGORY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_CATEGORY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }
}
