package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.converter.EntryToEntryVOConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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
    public void testDeleteByEntityWithExistingEntry() throws ServiceException {

        // given
        given(entryDAO.exists(entryVO.getId())).willReturn(true);

        // when
        entryService.deleteByEntity(entryVO);

        // then
        verify(entryDAO).delete(entryVO.getId());
    }

    @Test
    public void testDeleteByIdWithExistingEntry() throws ServiceException {

        // when
        entryService.deleteByID(entryVO.getId());

        // then
        verify(entryDAO).delete(entryVO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEntityWithNonExistingEntry() throws ServiceException {

        // given
        given(entryDAO.exists(entryVO.getId())).willReturn(false);
        given(entryVO.getId()).willReturn(1L);

        // when
        entryService.deleteByEntity(entryVO);

        // then
        // expected exception
        verify(entryDAO, never()).delete(any());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdWithNonExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        doThrow(IllegalArgumentException.class).when(entryDAO).delete(id);

        // when
        entryService.deleteByID(id);

        // then
        // expected exception
    }
}
