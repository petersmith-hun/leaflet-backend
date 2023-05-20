package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@ExtendWith(MockitoExtension.class)
public class AttachmentFacadeImplTest {

    @Mock(strictness = Mock.Strictness.LENIENT)
    private EntryService entryService;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private UploadedFileDAO uploadedFileDAO;

    @Mock
    private EntryVO entryVO;

    @Mock
    private UploadedFile uploadedFile;

    @InjectMocks
    private AttachmentFacadeImpl attachmentFacade;

    private AttachmentRequestVO attachmentRequestVO;

    @BeforeEach
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
        given(uploadedFileDAO.findByPathUUID(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFile));

        // when
        attachmentFacade.attachFileToEntry(attachmentRequestVO);

        // then
        verify(attachmentService).attachFileToEntry(uploadedFile, entryVO);
    }

    @Test
    public void shouldThrowExceptionOnAttachWhenEntryDoesNotExist() throws ServiceException {

        // given
        doThrow(EntityNotFoundException.class).when(entryService).getOne(attachmentRequestVO.getEntryID());

        // when
        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.attachFileToEntry(attachmentRequestVO));

        // then
        verify(attachmentService, never()).attachFileToEntry(any(UploadedFile.class), any(EntryVO.class));
    }

    @Test
    public void shouldThrowExceptionOnAttachWhenFileDoesNotExist() throws ServiceException {

        // given
        given(uploadedFileDAO.findByPathUUID(attachmentRequestVO.getPathUUID())).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.attachFileToEntry(attachmentRequestVO));

        // then
        verify(attachmentService, never()).attachFileToEntry(any(UploadedFile.class), any(EntryVO.class));
    }

    @Test
    public void shouldDetachFileFromEntry() throws ServiceException {

        // given
        given(entryService.getOne(attachmentRequestVO.getEntryID())).willReturn(entryVO);
        given(uploadedFileDAO.findByPathUUID(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFile));

        // when
        attachmentFacade.detachFileFromEntry(attachmentRequestVO);

        // then
        verify(entryService).getOne(attachmentRequestVO.getEntryID());
        verify(attachmentService).detachFileFromEntry(uploadedFile, entryVO);
    }

    @Test
    public void shouldThrowExceptionOnDetachWhenEntryDoesNotExist() throws ServiceException {

        // given
        doThrow(EntityNotFoundException.class).when(entryService).getOne(attachmentRequestVO.getEntryID());

        // when
        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.detachFileFromEntry(attachmentRequestVO));

        // then
        verify(attachmentService, never()).detachFileFromEntry(any(UploadedFile.class), any(EntryVO.class));
    }

    @Test
    public void shouldThrowExceptionOnDetachWhenFileDoesNotExist() throws ServiceException {

        // given
        given(uploadedFileDAO.findByPathUUID(attachmentRequestVO.getPathUUID())).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.detachFileFromEntry(attachmentRequestVO));

        // then
        verify(attachmentService, never()).detachFileFromEntry(any(UploadedFile.class), any(EntryVO.class));
    }
}
