package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CategoryVOToCategoryConverter;
import hu.psprog.leaflet.service.converter.EntryToEntryVOConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryServiceImplTest {

    @Mock
    private EntryDAO entryDAO;

    @Mock
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Mock
    private EntryToEntryVOConverter entryToEntryVOConverter;

    @Mock
    private CategoryVOToCategoryConverter categoryVOToCategoryConverter;

    @Mock
    private Entry entry;

    @Mock
    private EntryVO entryVO;

    @InjectMocks
    private EntryServiceImpl entryService;

    @Test
    public void testGetOneWithExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        given(entryDAO.findOne(id)).willReturn(entry);
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        EntryVO result = entryService.getOne(id);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryDAO).findOne(id);
        verify(entryToEntryVOConverter).convert(entry);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithNonExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        given(entryDAO.findOne(id)).willReturn(null);

        // when
        EntryVO result = entryService.getOne(id);

        // then
        // expected exception
        verify(entryDAO).findOne(id);
        verify(entryToEntryVOConverter, never()).convert(any());
    }

    @Test
    public void testGetAllWithPopulatedList() {

        // given
        List<EntryVO> documentVOList = Arrays.asList(entryVO, entryVO, entryVO);
        given(entryDAO.findAll()).willReturn(Arrays.asList(entry, entry, entry));
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        List<EntryVO> result = entryService.getAll();

        // then
        assertThat(result, equalTo(documentVOList));
        verify(entryDAO).findAll();
        verify(entryToEntryVOConverter, times(3)).convert(entry);
    }

    @Test
    public void testCount() {

        // given
        Long count = 5L;
        given(entryDAO.count()).willReturn(count);

        // when
        Long result = entryService.count();

        // then
        assertThat(result, equalTo(count));
    }

    @Test
    public void testGetEntityPage() {

        // given
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        given(entryVOToEntryConverter.convert(any(EntryVO.class))).willReturn(new Entry());
        given(entryDAO.findAll(any(Pageable.class))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getEntityPage(1, 10, OrderDirection.ASC, EntryVO.OrderBy.CREATED);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicEntries() {

        // given
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        given(entryVOToEntryConverter.convert(any(EntryVO.class))).willReturn(new Entry());
        given(entryDAO.findAll(any(Specifications.class), any(Pageable.class))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getPageOfPublicEntries(1, 10, OrderDirection.ASC, EntryVO.OrderBy.CREATED);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicEntriesUnderCategory() {

        // given
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        given(entryVOToEntryConverter.convert(any(EntryVO.class))).willReturn(new Entry());
        given(categoryVOToCategoryConverter.convert(any(CategoryVO.class))).willReturn(Category.getBuilder()
                .withId(1L)
                .build());
        given(entryDAO.findAll(any(Specifications.class), any(Pageable.class))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getPageOfPublicEntriesUnderCategory(new CategoryVO(),1, 10, OrderDirection.ASC, EntryVO.OrderBy.CREATED);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        given(entryDAO.save(entry)).willReturn(entry);

        // when
        Long result = entryService.createOne(entryVO);

        // then
        assertThat(result, equalTo(entry.getId()));
        verify(entryVOToEntryConverter).convert(entryVO);
        verify(entryDAO).save(entry);
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithFailure() throws ServiceException {

        // given
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        given(entryDAO.save(entry)).willReturn(null);

        // when
        entryService.createOne(entryVO);

        // then
        // expected exception
        verify(entryVOToEntryConverter).convert(entryVO);
        verify(entryDAO).save(entry);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCreateShouldThrowConstraintViolationException() throws ServiceException {

        // given
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(DataIntegrityViolationException.class).when(entryDAO).save(entry);

        // when
        entryService.createOne(entryVO);

        // then
        // exception expected
    }

    @Test(expected = ServiceException.class)
    public void testCreateShouldThrowServiceException() throws ServiceException {

        // given
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(IllegalArgumentException.class).when(entryDAO).save(entry);

        // when
        entryService.createOne(entryVO);

        // then
        // exception expected
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);
        given(entryDAO.updateOne(id, entry)).willReturn(entry);

        // when
        EntryVO result = entryService.updateOne(id, entryVO);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryVOToEntryConverter).convert(entryVO);
        verify(entryToEntryVOConverter).convert(entry);
        verify(entryDAO).updateOne(id, entry);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithFailure() throws ServiceException {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        given(entryDAO.updateOne(id, entry)).willReturn(null);

        // when
        EntryVO result = entryService.updateOne(id, entryVO);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryVOToEntryConverter).convert(entryVO);
        verify(entryToEntryVOConverter, never()).convert(entry);
        verify(entryDAO).updateOne(id, entry);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testUpdateShouldThrowConstraintViolationException() throws ServiceException {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(DataIntegrityViolationException.class).when(entryDAO).updateOne(id, entry);

        // when
        entryService.updateOne(id, entryVO);

        // then
        // exception expected
    }

    @Test(expected = ServiceException.class)
    public void testUpdateShouldThrowServiceException() throws ServiceException {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(IllegalArgumentException.class).when(entryDAO).updateOne(id, entry);

        // when
        entryService.updateOne(id, entryVO);

        // then
        // exception expected
    }

    @Test
    public void testFindByLinkWithExistingEntry() throws EntityNotFoundException {

        // given
        String link = "link-lflt-49-ut";
        given(entryDAO.findByLink(link)).willReturn(entry);
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        EntryVO result = entryService.findByLink(link);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryDAO).findByLink(link);
        verify(entryToEntryVOConverter).convert(entry);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindByLinkWithNonExistingEntry() throws EntityNotFoundException {

        // given
        String link = "link-lflt-49-ut";
        given(entryDAO.findByLink(link)).willReturn(null);

        // when
        EntryVO result = entryService.findByLink(link);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryDAO).findByLink(link);
        verify(entryToEntryVOConverter, never()).convert(entry);
    }

    @Test
    public void testDeleteByIdWithExistingEntry() throws ServiceException {

        // given
        given(entryDAO.exists(entryVO.getId())).willReturn(true);

        // when
        entryService.deleteByID(entryVO.getId());

        // then
        verify(entryDAO).delete(entryVO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdWithNonExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        given(entryDAO.exists(entryVO.getId())).willReturn(false);

        // when
        entryService.deleteByID(id);

        // then
        // expected exception
    }

    @Test
    public void shouldEnable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(true);

        // when
        entryService.enable(id);

        // then
        verify(entryDAO).enable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldEnableThrowEntityNotFoundException() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(false);

        // when
        entryService.enable(id);

        // then
        // exception expected;
    }

    @Test
    public void shouldDisable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(true);

        // when
        entryService.disable(id);

        // then
        verify(entryDAO).disable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldDisableThrowEntityNotFoundException() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(false);

        // when
        entryService.disable(id);

        // then
        // exception expected;
    }
}
