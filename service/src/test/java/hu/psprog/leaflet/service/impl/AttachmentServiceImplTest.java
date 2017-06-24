package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.converter.UploadedFileVOToUploadedFileConverter;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AttachmentServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class AttachmentServiceImplTest {

    private static final Long ENTRY_ID = 1L;
    private static final Long UPLOADED_FILE_ID = 15L;

    @Mock
    private EntryDAO entryDAO;

    @Mock
    private UploadedFileDAO uploadedFileDAO;

    @Mock
    private UploadedFileVOToUploadedFileConverter uploadedFileVOToUploadedFileConverter;

    @Mock
    private Entry mockedEntry;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    private EntryVO entryVO;
    private UploadedFileVO uploadedFileVO;
    private UploadedFile controlUploadedFile;
    private List<UploadedFile> attachments;

    @Before
    public void setup() {
        entryVO = EntryVO.getBuilder()
                .withId(ENTRY_ID)
                .withTitle("Test entry")
                .build();
        uploadedFileVO = UploadedFileVO.getBuilder()
                .withId(UPLOADED_FILE_ID)
                .withPath("images/stored_control_15.jpg")
                .build();
        controlUploadedFile = UploadedFile.getBuilder()
                .withStoredFilename("stored_control_15.jpg")
                .withOriginalFilename("original_control_15.jpg")
                .withPathUUID(UUID.randomUUID())
                .withId(UPLOADED_FILE_ID)
                .build();
        given(entryDAO.findOne(ENTRY_ID)).willReturn(mockedEntry);
    }

    @Test
    public void shouldNotAttachIfAlreadyAttached() throws ServiceException {

        // given
        prepareMocks(true);

        // when
        attachmentService.attachFileToEntry(uploadedFileVO, entryVO);

        // then
        assertResults(6, true, false);
    }

    @Test
    public void shouldAttachIfNotAlreadyAttached() throws ServiceException {

        // given
        prepareMocks(false);

        // when
        attachmentService.attachFileToEntry(uploadedFileVO, entryVO);

        // then
        assertResults(6, true, true);
    }

    @Test
    public void shouldNotDetachIfNotAttached() throws ServiceException {

        // given
        prepareMocks(false);

        // when
        attachmentService.detachFileFromEntry(uploadedFileVO, entryVO);

        // then
        assertResults(5, false, false);
    }

    @Test
    public void shouldDetachIfAttached() throws ServiceException {

        // given
        prepareMocks(true);

        // when
        attachmentService.detachFileFromEntry(uploadedFileVO, entryVO);

        // then
        assertResults(5, false, true);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnAttachIfUploadedFileDoesNotExist() throws EntityNotFoundException {

        // given
        prepareMocks(false);
        given(uploadedFileDAO.exists(UPLOADED_FILE_ID)).willReturn(false);

        // when
        attachmentService.attachFileToEntry(uploadedFileVO, entryVO);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnAttachIfEntryDoesNotExist() throws EntityNotFoundException {

        // given
        prepareMocks(false);
        given(entryDAO.exists(ENTRY_ID)).willReturn(false);

        // when
        attachmentService.attachFileToEntry(uploadedFileVO, entryVO);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnDetachIfUploadedFileDoesNotExist() throws EntityNotFoundException {

        // given
        prepareMocks(false);
        given(uploadedFileDAO.exists(UPLOADED_FILE_ID)).willReturn(false);

        // when
        attachmentService.detachFileFromEntry(uploadedFileVO, entryVO);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionOnDetachIfEntryDoesNotExist() throws EntityNotFoundException {

        // given
        prepareMocks(false);
        given(entryDAO.exists(ENTRY_ID)).willReturn(false);

        // when
        attachmentService.detachFileFromEntry(uploadedFileVO, entryVO);

        // then
        // expected exception
    }

    private void prepareMocks(boolean includeControlVO) {
        prepareAttachments(includeControlVO);
        given(mockedEntry.getAttachments()).willReturn(attachments);
        given(uploadedFileVOToUploadedFileConverter.convert(uploadedFileVO)).willReturn(controlUploadedFile);
        given(entryDAO.exists(ENTRY_ID)).willReturn(true);
        given(uploadedFileDAO.exists(UPLOADED_FILE_ID)).willReturn(true);
    }

    private void prepareAttachments(boolean includeControlVO) {

        attachments = new ArrayList<>();
        for (int cnt = 1; cnt <= 5; cnt++) {
            attachments.add(UploadedFile.getBuilder()
                    .withId((long) cnt)
                    .withPathUUID(UUID.randomUUID())
                    .withStoredFilename("stored_filename_" + cnt + ".jpg")
                    .withStoredFilename("original_filename_" + cnt + ".jpg")
                    .build());
        }

        if (includeControlVO) {
            attachments.add(controlUploadedFile);
        }
    }

    private void assertResults(int numberOfAttachments, boolean controlIncluded, boolean updateCalled) {
        assertThat(attachments.size(), equalTo(numberOfAttachments));
        assertThat(attachments.contains(controlUploadedFile), is(controlIncluded));
        verify(entryDAO).findOne(ENTRY_ID);
        verify(uploadedFileVOToUploadedFileConverter).convert(uploadedFileVO);
        if (updateCalled) {
            verify(entryDAO).updateAttachments(ENTRY_ID, attachments);
        } else {
            verify(entryDAO, never()).updateAttachments(anyLong(), anyList());
        }
    }
}
