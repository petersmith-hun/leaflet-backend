package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.dao.TagDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.service.converter.TagToTagVOConverter;
import hu.psprog.leaflet.service.converter.TagVOToTagConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link TagServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {

    private static final long TAG_ID = 15L;
    private static final long ENTRY_ID = 1L;
    private static final String CONTROL_TAG_TITLE = "Tag #15";

    @Mock
    private TagDAO tagDAO;

    @Mock
    private EntryDAO entryDAO;

    @Mock
    private TagToTagVOConverter tagToTagVOConverter;

    @Mock
    private TagVOToTagConverter tagVOToTagConverter;

    @Mock
    private Tag tag;

    @Mock
    private TagVO tagVO;

    @Mock
    private Entry entry;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag controlTag;
    private EntryVO inputEntryVO;
    private TagVO inputTagVO;
    private List<Tag> attachedTags;

    @Test
    public void testGetOneWithExistingTag() throws ServiceException {

        // given
        Long id = 1L;
        given(tagDAO.findOne(id)).willReturn(tag);
        given(tagToTagVOConverter.convert(tag)).willReturn(tagVO);

        // when
        TagVO result = tagService.getOne(id);

        // then
        assertThat(result, equalTo(tagVO));
        verify(tagDAO).findOne(id);
        verify(tagToTagVOConverter).convert(tag);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithNonExistingTag() throws ServiceException {

        // given
        Long id = 1L;
        given(tagDAO.findOne(id)).willReturn(null);

        // when
        tagService.getOne(id);

        // then
        // expected exception
        verify(tagDAO).findOne(id);
        verify(tagToTagVOConverter).convert(tag);
    }

    @Test
    public void testGetAllWithPopulatedList() {

        // given
        List<TagVO> documentVOList = Arrays.asList(tagVO, tagVO, tagVO);
        given(tagDAO.findAll()).willReturn(Arrays.asList(tag, tag, tag));
        given(tagToTagVOConverter.convert(tag)).willReturn(tagVO);

        // when
        List<TagVO> result = tagService.getAll();

        // then
        assertThat(result, equalTo(documentVOList));
        verify(tagDAO).findAll();
        verify(tagToTagVOConverter, times(3)).convert(tag);
    }

    @Test
    public void testGetAllWithEmptyList() {

        // given
        given(tagDAO.findAll()).willReturn(new LinkedList<>());

        // when
        List<TagVO> result = tagService.getAll();

        // then
        assertThat(result, empty());
        verify(tagDAO).findAll();
        verify(tagToTagVOConverter, never()).convert(any(Tag.class));
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(tagVOToTagConverter.convert(tagVO)).willReturn(tag);
        given(tagDAO.save(tag)).willReturn(tag);
        given(tag.getId()).willReturn(1L);

        // when
        Long result = tagService.createOne(tagVO);

        // then
        assertThat(result, equalTo(tag.getId()));
        verify(tagVOToTagConverter).convert(tagVO);
        verify(tagDAO).save(tag);
        verify(tag, atLeastOnce()).getId();
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithFailure() throws ServiceException {

        // given
        given(tagVOToTagConverter.convert(tagVO)).willReturn(tag);
        given(tagDAO.save(tag)).willReturn(null);

        // when
        tagService.createOne(tagVO);

        // then
        // expected exception
        verify(tagVOToTagConverter).convert(tagVO);
        verify(tagDAO).save(tag);
        verify(tag, never()).getId();
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(tagVOToTagConverter.convert(tagVO)).willReturn(tag);
        given(tagDAO.updateOne(id, tag)).willReturn(tag);
        given(tagToTagVOConverter.convert(tag)).willReturn(tagVO);

        // when
        TagVO result = tagService.updateOne(id, tagVO);

        // then
        assertThat(result, equalTo(tagVO));
        verify(tagToTagVOConverter).convert(tag);
        verify(tagVOToTagConverter).convert(tagVO);
        verify(tagDAO).updateOne(id, tag);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithFailure() throws ServiceException {

        // given
        Long id = 1L;
        given(tagVOToTagConverter.convert(tagVO)).willReturn(tag);
        given(tagDAO.updateOne(id, tag)).willReturn(null);

        // when
        tagService.updateOne(id, tagVO);

        // then
        // expected exception
        verify(tagToTagVOConverter, never()).convert(tag);
        verify(tagVOToTagConverter).convert(tagVO);
        verify(tagDAO).updateOne(id, tag);
    }

    @Test
    public void testDeleteByEntityWithExistingDocument() throws ServiceException {

        // given
        given(tagDAO.exists(tagVO.getId())).willReturn(true);

        // when
        tagService.deleteByEntity(tagVO);

        // then
        verify(tagDAO).delete(tagVO.getId());
    }


    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEntityWithNonExistingDocument() throws ServiceException {

        // given
        Long id = 1L;
        given(tagDAO.exists(tagVO.getId())).willReturn(false);
        given(tagVO.getId()).willReturn(1L);

        // when
        tagService.deleteByEntity(tagVO);

        // then
        verify(tagDAO, never()).delete(any(Long.class));
    }

    @Test
    public void testDeleteByIDWithExistingDocument() throws ServiceException {

        // given
        Long id = 1L;

        // when
        tagService.deleteByID(id);

        // then
        verify(tagDAO).delete(id);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIDWithNonExistingDocument() throws ServiceException {

        // given
        Long id = 1L;
        doThrow(IllegalArgumentException.class).when(tagDAO).delete(id);

        // when
        tagService.deleteByID(id);

        // then
        verify(tagDAO).delete(id);
    }

    @Test
    public void testEnableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(tagDAO.exists(id)).willReturn(true);

        // when
        tagService.enable(id);

        // then
        verify(tagDAO).exists(id);
        verify(tagDAO).enable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEnableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(tagDAO.exists(id)).willReturn(false);

        // when
        tagService.enable(id);

        // then
        verify(tagDAO).exists(id);
        verify(tagDAO, never()).enable(any(Long.class));
    }

    @Test
    public void testDisableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(tagDAO.exists(id)).willReturn(true);

        // when
        tagService.disable(id);

        // then
        verify(tagDAO).exists(id);
        verify(tagDAO).disable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDisableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(tagDAO.exists(id)).willReturn(false);

        // when
        tagService.disable(id);

        // then
        verify(tagDAO).exists(id);
        verify(tagDAO, never()).disable(any(Long.class));
    }

    @Test
    public void shouldNotAttachTagIfAlreadyAttached() throws ServiceException {

        // given
        prepareMocks(true);

        // when
        tagService.attachTagToEntry(inputTagVO, inputEntryVO);

        // then
        assertResults(6, true, false);
    }

    @Test
    public void shouldAttachTagIfNotAlreadyAttached() throws ServiceException {

        // given
        prepareMocks(false);

        // when
        tagService.attachTagToEntry(inputTagVO, inputEntryVO);

        // then
        assertResults(6, true, true);
    }

    @Test
    public void shouldNotDetachTagIfNotAttached() throws ServiceException {

        // given
        prepareMocks(false);

        // when
        tagService.detachTagFromEntry(inputTagVO, inputEntryVO);

        // then
        assertResults(5, false, false);
    }

    @Test
    public void shouldDetachTagIfAttached() throws ServiceException {

        // given
        prepareMocks(true);

        // when
        tagService.detachTagFromEntry(inputTagVO, inputEntryVO);

        // then
        assertResults(5, false, true);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnAttachIfTagDoesNotExist() throws ServiceException {

        // given
        prepareMocks(false);
        given(tagDAO.exists(TAG_ID)).willReturn(false);

        // when
        tagService.attachTagToEntry(inputTagVO, inputEntryVO);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnAttachIfEntryDoesNotExist() throws ServiceException {

        // given
        prepareMocks(false);
        given(entryDAO.exists(ENTRY_ID)).willReturn(false);

        // when
        tagService.attachTagToEntry(inputTagVO, inputEntryVO);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnDetachIfTagDoesNotExist() throws ServiceException {

        // given
        prepareMocks(false);
        given(tagDAO.exists(TAG_ID)).willReturn(false);

        // when
        tagService.detachTagFromEntry(inputTagVO, inputEntryVO);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnDetachIfEntryDoesNotExist() throws ServiceException {

        // given
        prepareMocks(false);
        given(entryDAO.exists(ENTRY_ID)).willReturn(false);

        // when
        tagService.detachTagFromEntry(inputTagVO, inputEntryVO);

        // then
        // expected exception
    }

    private void prepareMocks(boolean includeControlVO) {
        prepareTestEntities();
        prepareTags(includeControlVO);
        given(entryDAO.findOne(anyLong())).willReturn(entry);
        given(entry.getTags()).willReturn(attachedTags);
        given(tagVOToTagConverter.convert(inputTagVO)).willReturn(controlTag);
        given(entryDAO.exists(ENTRY_ID)).willReturn(true);
        given(tagDAO.exists(TAG_ID)).willReturn(true);
    }

    private void prepareTags(boolean includeControlVO) {

        attachedTags = new ArrayList<>();
        for (int cnt = 1; cnt <= 5; cnt++) {
            attachedTags.add(new Tag.Builder()
                    .withId((long) cnt)
                    .withTitle("Tag #" + cnt)
                    .createTag());
        }

        if (includeControlVO) {
            attachedTags.add(controlTag);
        }
    }

    private void prepareTestEntities() {
        inputTagVO = new TagVO.Builder()
                .withId(TAG_ID)
                .withTitle(CONTROL_TAG_TITLE)
                .createTagVO();
        inputEntryVO = new EntryVO.Builder()
                .withId(ENTRY_ID)
                .withTitle("Test entry")
                .createEntryVO();
        controlTag = new Tag.Builder()
                .withId(TAG_ID)
                .withTitle(CONTROL_TAG_TITLE)
                .createTag();
    }

    private void assertResults(int numberOfTags, boolean controlIncluded, boolean updateCalled) {
        assertThat(attachedTags.size(), equalTo(numberOfTags));
        assertThat(attachedTags.contains(controlTag), is(controlIncluded));
        verify(entryDAO).findOne(ENTRY_ID);
        verify(tagVOToTagConverter).convert(inputTagVO);
        if (updateCalled) {
            verify(entryDAO).updateTags(ENTRY_ID, attachedTags);
        } else {
            verify(entryDAO, never()).updateTags(anyLong(), anyList());
        }
    }
}
