package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CategoryFacade;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CategoriesController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CategoriesControllerTest extends AbstractControllerBaseTest {

    private static final long CATEGORY_ID = 1L;
    private static final String LOCATION_HEADER = "/categories/1";

    @Mock
    private CategoryFacade categoryFacade;

    @InjectMocks
    private CategoriesController controller;

    @BeforeEach
    public void setup() {
        super.setup();
        given(conversionService.convert(Collections.emptyList(), CategoryListDataModel.class)).willReturn(CATEGORY_LIST_DATA_MODEL);
        given(conversionService.convert(CATEGORY_VO, CategoryDataModel.class)).willReturn(CATEGORY_DATA_MODEL);
    }

    @Test
    public void shouldGetAllCategories() {

        // given
        given(categoryFacade.getAll()).willReturn(Collections.emptyList());

        // when
        ResponseEntity<CategoryListDataModel> result = controller.getAllCategories();

        // then
        assertResponse(result, HttpStatus.OK, CATEGORY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetAllPublicCategories() {

        // given
        given(categoryFacade.getAllPublic()).willReturn(Collections.emptyList());

        // when
        ResponseEntity<CategoryListDataModel> result = controller.getPublicCategories();

        // then
        assertResponse(result, HttpStatus.OK, CATEGORY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetCategory() throws ServiceException, ResourceNotFoundException {

        // given
        given(categoryFacade.getOne(CATEGORY_ID)).willReturn(CATEGORY_VO);

        // when
        ResponseEntity<CategoryDataModel> result = controller.getCategory(CATEGORY_ID);

        // then
        assertResponse(result, HttpStatus.OK, CATEGORY_DATA_MODEL);
    }

    @Test
    public void shouldGetCategoryWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(categoryFacade).getOne(CATEGORY_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.getCategory(CATEGORY_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldCreate() throws ServiceException, RequestCouldNotBeFulfilledException {

        // when
        given(conversionService.convert(CATEGORY_CREATE_REQUEST_MODEL, CategoryVO.class)).willReturn(CATEGORY_VO);
        given(categoryFacade.createOne(CATEGORY_VO)).willReturn(CATEGORY_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createCategory(CATEGORY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, CATEGORY_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldCreateWithServiceException() throws ServiceException {

        // when
        doThrow(ServiceException.class).when(categoryFacade).createOne(any());

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.createCategory(CATEGORY_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateWithValidationError() throws RequestCouldNotBeFulfilledException {

        // when
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createCategory(CATEGORY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldUpdate() throws ServiceException, ResourceNotFoundException {

        // when
        given(conversionService.convert(CATEGORY_CREATE_REQUEST_MODEL, CategoryVO.class)).willReturn(CATEGORY_VO);
        given(categoryFacade.updateOne(CATEGORY_ID, CATEGORY_VO)).willReturn(CATEGORY_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateCategory(CATEGORY_ID, CATEGORY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, CATEGORY_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateWithServiceException() throws ServiceException {

        // when
        doThrow(ServiceException.class).when(categoryFacade).updateOne(anyLong(), any());

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.updateCategory(CATEGORY_ID, CATEGORY_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateWithValidationError() throws ResourceNotFoundException {

        // when
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateCategory(CATEGORY_ID, CATEGORY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldChangeStatus() throws ServiceException, ResourceNotFoundException {

        // given
        given(categoryFacade.changeStatus(CATEGORY_ID)).willReturn(CATEGORY_VO);

        // when
        ResponseEntity<CategoryDataModel> result = controller.changeStatus(CATEGORY_ID);

        // then
        assertResponse(result, HttpStatus.CREATED, CATEGORY_DATA_MODEL);
    }

    @Test
    public void shouldChangeStatusWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(categoryFacade).changeStatus(CATEGORY_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.changeStatus(CATEGORY_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteCategory() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteCategory(CATEGORY_ID);

        // then
        verify(categoryFacade).deletePermanently(CATEGORY_ID);
    }

    @Test
    public void shouldDeleteCategoryWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(categoryFacade).deletePermanently(CATEGORY_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.deleteCategory(CATEGORY_ID));

        // then
        // exception expected
    }
}