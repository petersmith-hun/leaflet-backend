package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AttachmentServiceImpl} class.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AttachmentServiceImplTest {

    private static final Long ENTRY_ID = 1L;
    private static final Long UPLOADED_FILE_ID = 15L;

    @Mock(lenient = true)
    private EntryDAO entryDAO;

    @Mock(lenient = true)
    private Entry mockedEntry;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    private EntryVO entryVO;
    private UploadedFile controlUploadedFile;
    private List<UploadedFile> attachments;

    @BeforeEach
    public void setup() {
        entryVO = EntryVO.getBuilder()
                .withId(ENTRY_ID)
                .withTitle("Test entry")
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
        attachmentService.attachFileToEntry(controlUploadedFile, entryVO);

        // then
        assertResults(6, true, false);
    }

    @Test
    public void shouldAttachIfNotAlreadyAttached() throws ServiceException {

        // given
        prepareMocks(false);

        // when
        attachmentService.attachFileToEntry(controlUploadedFile, entryVO);

        // then
        assertResults(6, true, true);
    }

    @Test
    public void shouldNotDetachIfNotAttached() throws ServiceException {

        // given
        prepareMocks(false);

        // when
        attachmentService.detachFileFromEntry(controlUploadedFile, entryVO);

        // then
        assertResults(5, false, false);
    }

    @Test
    public void shouldDetachIfAttached() throws ServiceException {

        // given
        prepareMocks(true);

        // when
        attachmentService.detachFileFromEntry(controlUploadedFile, entryVO);

        // then
        assertResults(5, false, true);
    }

    @Test
    public void shouldThrowExceptionOnAttachIfEntryDoesNotExist() {

        // given
        prepareMocks(false);
        given(entryDAO.exists(ENTRY_ID)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> attachmentService.attachFileToEntry(controlUploadedFile, entryVO));

        // then
        // expected exception
    }

    @Test
    public void shouldThrowExceptionOnDetachIfEntryDoesNotExist() {

        // given
        prepareMocks(false);
        given(entryDAO.exists(ENTRY_ID)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> attachmentService.detachFileFromEntry(controlUploadedFile, entryVO));

        // then
        // expected exception
    }

    private void prepareMocks(boolean includeControlVO) {
        prepareAttachments(includeControlVO);
        given(mockedEntry.getAttachments()).willReturn(attachments);
        given(entryDAO.exists(ENTRY_ID)).willReturn(true);
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
        if (updateCalled) {
            verify(entryDAO).updateAttachments(ENTRY_ID, attachments);
        } else {
            verify(entryDAO, never()).updateAttachments(anyLong(), anyList());
        }
    }
}
