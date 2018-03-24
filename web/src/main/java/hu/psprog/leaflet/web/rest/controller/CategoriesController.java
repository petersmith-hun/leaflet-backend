package hu.psprog.leaflet.web.rest.controller;

import com.codahale.metrics.annotation.Timed;
import hu.psprog.leaflet.api.rest.request.category.CategoryCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CategoryFacade;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
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

    private CategoryFacade categoryFacade;

    @Autowired
    public CategoriesController(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    /**
     * GET /categories
     * Returns list of all existing categories.
     *
     * @return list of categories
     */
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public ResponseEntity<CategoryListDataModel> getAllCategories() {

        List<CategoryVO> categories = categoryFacade.getAll();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(categories, CategoryListDataModel.class));
    }

    /**
     * GET /categories/public
     * Returns list of public categories.
     *
     * @return list of public categories
     */
    @RequestMapping(value = PATH_PUBLIC, method = RequestMethod.GET)
    @Timed
    public ResponseEntity<CategoryListDataModel> getPublicCategories() {

        List<CategoryVO> categories = categoryFacade.getAllPublic();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(categories, CategoryListDataModel.class));
    }

    /**
     * GET /categories/{id}
     * Returns category identified by given ID.
     *
     * @param id ID of an existing category
     * @return category data if requested category exists
     */
    @RequestMapping(value = PATH_PART_ID, method = RequestMethod.GET)
    public ResponseEntity<CategoryDataModel> getCategory(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            CategoryVO category = categoryFacade.getOne(id);

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(category, CategoryDataModel.class));
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
    public ResponseEntity<BaseBodyDataModel> createCategory(@RequestBody @Valid CategoryCreateRequestModel categoryCreateRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                CategoryVO createdCategory = categoryFacade.createOne(conversionService.convert(categoryCreateRequestModel, CategoryVO.class));

                return ResponseEntity
                        .created(buildLocation(createdCategory.getId()))
                        .body(conversionService.convert(createdCategory, CategoryDataModel.class));
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
    public ResponseEntity<BaseBodyDataModel> updateCategory(@PathVariable(PATH_VARIABLE_ID) Long id,
                                       @RequestBody @Valid CategoryCreateRequestModel categoryCreateRequestModel,
                                       BindingResult bindingResult) throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                CategoryVO updatedCategory = categoryFacade.updateOne(id, conversionService.convert(categoryCreateRequestModel, CategoryVO.class));

                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(updatedCategory, CategoryDataModel.class));
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
    public ResponseEntity<CategoryDataModel> changeStatus(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            CategoryVO updatedCategoryVO = categoryFacade.changeStatus(id);

            return ResponseEntity
                    .created(buildLocation(id))
                    .body(conversionService.convert(updatedCategoryVO, CategoryDataModel.class));
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
            categoryFacade.deletePermanently(id);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_CATEGORY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_CATEGORY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_CATEGORIES + "/" + id);
    }
}
