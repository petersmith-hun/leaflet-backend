package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.category.CategoryCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance tests for {@code /categories} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuite
public class CategoriesControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_CATEGORIES = 3;
    private static final int NUMBER_OF_PUBLIC_CATEGORIES = 2;
    private static final long CONTROL_CATEGORY_ID = 1L;
    private static final String CONTROL_CATEGORY_1 = "category-1";

    @Autowired
    private CategoryBridgeService categoryBridgeService;

    @Test
    public void shouldGetAllCategories() throws CommunicationFailureException {

        // when
        CategoryListDataModel result = categoryBridgeService.getAllCategories();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getCategories().size(), equalTo(NUMBER_OF_ALL_CATEGORIES));
    }

    @Test
    public void shouldGetPublicCategories() throws CommunicationFailureException {

        // when
        CategoryListDataModel result = categoryBridgeService.getPublicCategories();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getCategories().size(), equalTo(NUMBER_OF_PUBLIC_CATEGORIES));
        assertThat(result.getCategories().stream().allMatch(CategoryDataModel::isEnabled), is(true));
    }

    @Test
    public void shouldGetCategoryByID() throws CommunicationFailureException {

        // given
        CategoryDataModel control = getControl(CONTROL_CATEGORY_1, CategoryDataModel.class);

        // when
        CategoryDataModel result = categoryBridgeService.getCategory(CONTROL_CATEGORY_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test
    @ResetDatabase
    public void shouldCreateCategory() throws CommunicationFailureException {

        // given
        CategoryCreateRequestModel categoryCreateRequestModel = new CategoryCreateRequestModel();
        categoryCreateRequestModel.setTitle("New category");
        categoryCreateRequestModel.setDescription("New category description");

        // when
        CategoryDataModel result = categoryBridgeService.createCategory(categoryCreateRequestModel);

        // then
        assertModifiedCategory(result.getId(), categoryCreateRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldUpdateCategory() throws CommunicationFailureException {

        // given
        CategoryCreateRequestModel categoryCreateRequestModel = new CategoryCreateRequestModel();
        categoryCreateRequestModel.setTitle("Updated category");
        categoryCreateRequestModel.setDescription("Updated category description");

        // when
        categoryBridgeService.updateCategory(CONTROL_CATEGORY_ID, categoryCreateRequestModel);

        // then
        assertModifiedCategory(CONTROL_CATEGORY_ID, categoryCreateRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldChangeStatus() throws CommunicationFailureException {

        // when
        categoryBridgeService.changeStatus(CONTROL_CATEGORY_ID);

        // then
        assertThat(categoryBridgeService.getCategory(CONTROL_CATEGORY_ID).isEnabled(), is(false));
    }

    @Test(expected = ResourceNotFoundException.class)
    @ResetDatabase
    public void shouldDeleteCategory() throws CommunicationFailureException {

        // given
        Long categoryIDToDelete = 3L;

        // when
        categoryBridgeService.deleteCategory(categoryIDToDelete);

        // then
        // this call should cause exception
        categoryBridgeService.getCategory(categoryIDToDelete);
    }

    private void assertModifiedCategory(Long categoryID, CategoryCreateRequestModel expected) throws CommunicationFailureException {

        CategoryDataModel current = categoryBridgeService.getCategory(categoryID);
        assertThat(current, notNullValue());
        assertThat(current.getTitle(), equalTo(expected.getTitle()));
        assertThat(current.getDescription(), equalTo(expected.getDescription()));
    }
}
