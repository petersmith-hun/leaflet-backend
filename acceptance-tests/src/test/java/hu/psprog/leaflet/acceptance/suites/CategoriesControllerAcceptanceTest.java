package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.category.CategoryCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
        assertThat(result.categories().size(), equalTo(NUMBER_OF_ALL_CATEGORIES));
    }

    @Test
    public void shouldGetPublicCategories() throws CommunicationFailureException {

        // when
        CategoryListDataModel result = categoryBridgeService.getPublicCategories();

        // then
        assertThat(result, notNullValue());
        assertThat(result.categories().size(), equalTo(NUMBER_OF_PUBLIC_CATEGORIES));
        assertThat(result.categories().stream().allMatch(CategoryDataModel::enabled), is(true));
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
        assertModifiedCategory(result.id(), categoryCreateRequestModel);
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
        assertThat(categoryBridgeService.getCategory(CONTROL_CATEGORY_ID).enabled(), is(false));
    }

    @Test
    @ResetDatabase
    public void shouldDeleteCategory() throws CommunicationFailureException {

        // given
        Long categoryIDToDelete = 3L;

        // when
        categoryBridgeService.deleteCategory(categoryIDToDelete);

        // then
        // this call should cause exception
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryBridgeService.getCategory(categoryIDToDelete));
    }

    private void assertModifiedCategory(Long categoryID, CategoryCreateRequestModel expected) throws CommunicationFailureException {

        CategoryDataModel current = categoryBridgeService.getCategory(categoryID);
        assertThat(current, notNullValue());
        assertThat(current.title(), equalTo(expected.getTitle()));
        assertThat(current.description(), equalTo(expected.getDescription()));
    }
}
