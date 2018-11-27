package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FileManagementFacade;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AttachmentFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class AttachmentFacadeImplTest {

    @Mock
    private EntryService entryService;

    @Mock
    private FileManagementFacade fileManagementFacade;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private EntryVO entryVO;

    @Mock
    private UploadedFileVO uploadedFileVO;

    @InjectMocks
    private AttachmentFacadeImpl attachmentFacade;

    private AttachmentRequestVO attachmentRequestVO;

    @Before
    public void setup() {
        attachmentRequestVO = AttachmentRequestVO.getBuilder()
                .withEntryID(1L)
                .withPathUUID(UUID.randomUUID())
                .build();
    }

    @Test
    public void shouldAttachFileToEntry() throws ServiceException {

        // given
        given(entryService.getOne(attachmentRequestVO.getEntryID())).willReturn(entryVO);
        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));

        // when
        attachmentFacade.attachFileToEntry(attachmentRequestVO);

        // then
        verify(entryService).getOne(attachmentRequestVO.getEntryID());
        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
        verify(attachmentService).attachFileToEntry(uploadedFileVO, entryVO);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnAttachWhenEntryDoesNotExist() throws ServiceException {

        // given
        doThrow(EntityNotFoundException.class).when(entryService).getOne(attachmentRequestVO.getEntryID());
        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));

        // when
        attachmentFacade.attachFileToEntry(attachmentRequestVO);

        // then
        verify(entryService).getOne(attachmentRequestVO.getEntryID());
        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
        verify(attachmentService, never()).attachFileToEntry(any(UploadedFileVO.class), any(EntryVO.class));
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnAttachWhenFileDoesNotExist() throws ServiceException {

        // given
        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.empty());

        // when
        attachmentFacade.attachFileToEntry(attachmentRequestVO);

        // then
        verify(entryService, never()).getOne(attachmentRequestVO.getEntryID());
        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
        verify(attachmentService, never()).attachFileToEntry(any(UploadedFileVO.class), any(EntryVO.class));
    }

    @Test
    public void shouldDetachFileFromEntry() throws ServiceException {

        // given
        given(entryService.getOne(attachmentRequestVO.getEntryID())).willReturn(entryVO);
        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));

        // when
        attachmentFacade.detachFileFromEntry(attachmentRequestVO);

        // then
        verify(entryService).getOne(attachmentRequestVO.getEntryID());
        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
        verify(attachmentService).detachFileFromEntry(uploadedFileVO, entryVO);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnDetachWhenEntryDoesNotExist() throws ServiceException {

        // given
        doThrow(EntityNotFoundException.class).when(entryService).getOne(attachmentRequestVO.getEntryID());
        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));

        // when
        attachmentFacade.detachFileFromEntry(attachmentRequestVO);

        // then
        verify(entryService).getOne(attachmentRequestVO.getEntryID());
        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
        verify(attachmentService, never()).detachFileFromEntry(any(UploadedFileVO.class), any(EntryVO.class));
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnDetachWhenFileDoesNotExist() throws ServiceException {

        // given
        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.empty());

        // when
        attachmentFacade.detachFileFromEntry(attachmentRequestVO);

        // then
        verify(entryService, never()).getOne(attachmentRequestVO.getEntryID());
        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
        verify(attachmentService, never()).detachFileFromEntry(any(UploadedFileVO.class), any(EntryVO.class));
    }
}
