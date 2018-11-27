package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.CategoryService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CategoryFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryFacadeImplTest {

    private static final long CATEGORY_ID = 2L;
    private static final CategoryVO CATEGORY_VO = CategoryVO.getBuilder()
            .withId(CATEGORY_ID)
            .build();

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryFacadeImpl categoryFacade;

    @Test
    public void shouldGetOne() throws ServiceException {

        // when
        categoryFacade.getOne(CATEGORY_ID);

        // then
        verify(categoryService).getOne(CATEGORY_ID);
    }

    @Test
    public void shouldGetAll() {

        // when
        categoryFacade.getAll();

        // then
        verify(categoryService).getAll();
    }

    @Test
    public void shouldGetAllPublic() {

        // when
        categoryFacade.getAllPublic();

        // then
        verify(categoryService).getAllPublic();
    }

    @Test
    public void shouldCount() {

        // given
        long count = 10;
        given(categoryService.count()).willReturn(count);

        // when
        long result = categoryFacade.count();

        // then
        assertThat(result, equalTo(count));
    }

    @Test
    public void shouldCreateOne() throws ServiceException {

        // given
        given(categoryService.createOne(CATEGORY_VO)).willReturn(CATEGORY_ID);

        // when
        categoryFacade.createOne(CATEGORY_VO);

        // then
        verify(categoryService).createOne(CATEGORY_VO);
        verify(categoryService).getOne(CATEGORY_ID);
    }

    @Test
    public void shouldUpdateOne() throws ServiceException {

        // when
        categoryFacade.updateOne(CATEGORY_ID, CATEGORY_VO);

        // then
        verify(categoryService).updateOne(CATEGORY_ID, CATEGORY_VO);
        verify(categoryService).getOne(CATEGORY_ID);
    }

    @Test
    public void shouldDeletePermanently() throws ServiceException {

        // when
        categoryFacade.deletePermanently(CATEGORY_ID);

        // then
        verify(categoryService).deleteByID(CATEGORY_ID);
    }

    @Test
    public void shouldChangeStatusToEnabled() throws ServiceException {

        // given
        given(categoryService.getOne(CATEGORY_ID)).willReturn(CATEGORY_VO);

        // when
        categoryFacade.changeStatus(CATEGORY_ID);

        // then
        verify(categoryService).enable(CATEGORY_ID);
    }

    @Test
    public void shouldChangeStatusToDisabled() throws ServiceException {

        // given
        given(categoryService.getOne(CATEGORY_ID)).willReturn(CategoryVO.getBuilder()
                .withEnabled(true)
                .build());

        // when
        categoryFacade.changeStatus(CATEGORY_ID);

        // then
        verify(categoryService).disable(CATEGORY_ID);
    }
}