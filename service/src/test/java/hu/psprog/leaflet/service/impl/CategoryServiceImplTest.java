package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CategoryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.repository.specification.CategorySpecification;
import hu.psprog.leaflet.service.converter.CategoryToCategoryVOConverter;
import hu.psprog.leaflet.service.converter.CategoryVOToCategoryConverter;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    
    @Mock(lenient = true)
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
        given(categoryDAO.findById(id)).willReturn(Optional.of(category));
        given(categoryToCategoryVOConverter.convert(category)).willReturn(categoryVO);

        // when
        CategoryVO result = categoryService.getOne(id);

        // then
        assertThat(result, equalTo(categoryVO));
        verify(categoryDAO).findById(id);
        verify(categoryToCategoryVOConverter).convert(category);
    }

    @Test
    public void testGetOneWithNonExistingEntry() {

        // given
        Long id = 1L;
        given(categoryDAO.findById(id)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryService.getOne(id));

        // then
        // expected exception
        verify(categoryDAO).findById(id);
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

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(categoryVOToCategoryConverter.convert(categoryVO)).willReturn(category);
        given(categoryToCategoryVOConverter.convert(category)).willReturn(categoryVO);
        given(categoryDAO.updateOne(id, category)).willReturn(Optional.of(category));

        // when
        CategoryVO result = categoryService.updateOne(id, categoryVO);

        // then
        assertThat(result, equalTo(categoryVO));
        verify(categoryVOToCategoryConverter).convert(categoryVO);
        verify(categoryToCategoryVOConverter).convert(category);
        verify(categoryDAO).updateOne(id, category);
    }

    @Test
    public void testUpdateOneWithFailure() {

        // given
        Long id = 1L;
        given(categoryVOToCategoryConverter.convert(categoryVO)).willReturn(category);
        given(categoryDAO.updateOne(id, category)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryService.updateOne(id, categoryVO));

        // then
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

    @Test
    public void testDeleteByIdWithNonExistingEntry() {

        // given
        Long id = 1L;
        given(categoryDAO.exists(categoryVO.getId())).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryService.deleteByID(id));

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

    @Test
    public void shouldEnableThrowEntityNotFoundException() {

        // given
        Long id = 1L;
        given(categoryDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryService.enable(id));

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

    @Test
    public void shouldDisableThrowEntityNotFoundException() {

        // given
        Long id = 1L;
        given(categoryDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryService.disable(id));

        // then
        // exception expected;
    }
}
