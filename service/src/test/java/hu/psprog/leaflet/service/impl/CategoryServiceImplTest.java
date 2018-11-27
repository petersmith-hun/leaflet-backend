package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CategoryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.repository.specification.CategorySpecification;
import hu.psprog.leaflet.service.converter.CategoryToCategoryVOConverter;
import hu.psprog.leaflet.service.converter.CategoryVOToCategoryConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CategoryServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {
    
    @Mock
    private CategoryDAO categoryDAO;
    
    @Mock
    private CategoryVOToCategoryConverter categoryVOToCategoryConverter;
    
    @Mock
    private CategoryToCategoryVOConverter categoryToCategoryVOConverter;
    
    @Mock
    private Category category;
    
    @Mock
    private CategoryVO categoryVO;
    
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void testGetOneWithExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        given(categoryDAO.findOne(id)).willReturn(category);
        given(categoryToCategoryVOConverter.convert(category)).willReturn(categoryVO);

        // when
        CategoryVO result = categoryService.getOne(id);

        // then
        assertThat(result, equalTo(categoryVO));
        verify(categoryDAO).findOne(id);
        verify(categoryToCategoryVOConverter).convert(category);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithNonExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        given(categoryDAO.findOne(id)).willReturn(null);

        // when
        categoryService.getOne(id);

        // then
        // expected exception
        verify(categoryDAO).findOne(id);
        verify(categoryToCategoryVOConverter, never()).convert(any());
    }

    @Test
    public void testGetAllWithPopulatedList() {

        // given
        List<CategoryVO> categoryVOList = Arrays.asList(categoryVO, categoryVO, categoryVO);
        given(categoryDAO.findAll()).willReturn(Arrays.asList(category, category, category));
        given(categoryToCategoryVOConverter.convert(category)).willReturn(categoryVO);

        // when
        List<CategoryVO> result = categoryService.getAll();

        // then
        assertThat(result, equalTo(categoryVOList));
        verify(categoryDAO).findAll();
        verify(categoryToCategoryVOConverter, times(3)).convert(category);
    }

    @Test
    public void testGetPublicCategories() {

        // given
        List<CategoryVO> categoryVOList = Arrays.asList(categoryVO, categoryVO, categoryVO);
        given(categoryDAO.findAll(CategorySpecification.IS_ENABLED)).willReturn(Arrays.asList(category, category, category));
        given(categoryToCategoryVOConverter.convert(category)).willReturn(categoryVO);

        // when
        List<CategoryVO> result = categoryService.getAllPublic();

        // then
        assertThat(result, equalTo(categoryVOList));
        verify(categoryDAO).findAll(CategorySpecification.IS_ENABLED);
        verify(categoryToCategoryVOConverter, times(3)).convert(category);
    }

    @Test
    public void testCount() {

        // given
        Long count = 5L;
        given(categoryDAO.count()).willReturn(count);

        // when
        Long result = categoryService.count();

        // then
        assertThat(result, equalTo(count));
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(categoryVOToCategoryConverter.convert(categoryVO)).willReturn(category);
        given(categoryDAO.save(category)).willReturn(category);

        // when
        Long result = categoryService.createOne(categoryVO);

        // then
        assertThat(result, equalTo(category.getId()));
        verify(categoryVOToCategoryConverter).convert(categoryVO);
        verify(categoryDAO).save(category);
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithFailure() throws ServiceException {

        // given
        given(categoryVOToCategoryConverter.convert(categoryVO)).willReturn(category);
        given(categoryDAO.save(category)).willReturn(null);

        // when
        categoryService.createOne(categoryVO);

        // then
        // expected exception
        verify(categoryVOToCategoryConverter).convert(categoryVO);
        verify(categoryDAO).save(category);
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(categoryVOToCategoryConverter.convert(categoryVO)).willReturn(category);
        given(categoryToCategoryVOConverter.convert(category)).willReturn(categoryVO);
        given(categoryDAO.updateOne(id, category)).willReturn(category);

        // when
        CategoryVO result = categoryService.updateOne(id, categoryVO);

        // then
        assertThat(result, equalTo(categoryVO));
        verify(categoryVOToCategoryConverter).convert(categoryVO);
        verify(categoryToCategoryVOConverter).convert(category);
        verify(categoryDAO).updateOne(id, category);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithFailure() throws ServiceException {

        // given
        Long id = 1L;
        given(categoryVOToCategoryConverter.convert(categoryVO)).willReturn(category);
        given(categoryDAO.updateOne(id, category)).willReturn(null);

        // when
        CategoryVO result = categoryService.updateOne(id, categoryVO);

        // then
        assertThat(result, equalTo(categoryVO));
        verify(categoryVOToCategoryConverter).convert(categoryVO);
        verify(categoryToCategoryVOConverter, never()).convert(category);
        verify(categoryDAO).updateOne(id, category);
    }

    @Test
    public void testDeleteByIdWithExistingEntry() throws ServiceException {

        // given
        given(categoryDAO.exists(categoryVO.getId())).willReturn(true);

        // when
        categoryService.deleteByID(categoryVO.getId());

        // then
        verify(categoryDAO).delete(categoryVO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdWithNonExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        given(categoryDAO.exists(categoryVO.getId())).willReturn(false);

        // when
        categoryService.deleteByID(id);

        // then
        // expected exception
    }

    @Test
    public void shouldEnable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(categoryDAO.exists(id)).willReturn(true);

        // when
        categoryService.enable(id);

        // then
        verify(categoryDAO).enable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldEnableThrowEntityNotFoundException() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(categoryDAO.exists(id)).willReturn(false);

        // when
        categoryService.enable(id);

        // then
        // exception expected;
    }

    @Test
    public void shouldDisable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(categoryDAO.exists(id)).willReturn(true);

        // when
        categoryService.disable(id);

        // then
        verify(categoryDAO).disable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldDisableThrowEntityNotFoundException() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(categoryDAO.exists(id)).willReturn(false);

        // when
        categoryService.disable(id);

        // then
        // exception expected;
    }
}
