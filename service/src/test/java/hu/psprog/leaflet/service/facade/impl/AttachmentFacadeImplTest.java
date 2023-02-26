package hu.psprog.leaflet.service.facade.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link AttachmentFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AttachmentFacadeImplTest {
//
//    @Mock
//    private EntryService entryService;
//
//    @Mock
//    private FileManagementFacade fileManagementFacade;
//
//    @Mock
//    private AttachmentService attachmentService;
//
//    @Mock
//    private EntryVO entryVO;
//
//    @Mock
//    private UploadedFileVO uploadedFileVO;
//
//    @InjectMocks
//    private AttachmentFacadeImpl attachmentFacade;
//
//    private AttachmentRequestVO attachmentRequestVO;
//
//    @BeforeEach
//    public void setup() {
//        attachmentRequestVO = AttachmentRequestVO.getBuilder()
//                .withEntryID(1L)
//                .withPathUUID(UUID.randomUUID())
//                .build();
//    }
//
//    @Test
//    public void shouldAttachFileToEntry() throws ServiceException {
//
//        // given
//        given(entryService.getOne(attachmentRequestVO.getEntryID())).willReturn(entryVO);
//        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));
//
//        // when
//        attachmentFacade.attachFileToEntry(attachmentRequestVO);
//
//        // then
//        verify(entryService).getOne(attachmentRequestVO.getEntryID());
//        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
//        verify(attachmentService).attachFileToEntry(uploadedFileVO, entryVO);
//    }
//
//    @Test
//    public void shouldThrowExceptionOnAttachWhenEntryDoesNotExist() throws ServiceException {
//
//        // given
//        doThrow(EntityNotFoundException.class).when(entryService).getOne(attachmentRequestVO.getEntryID());
//        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));
//
//        // when
//        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.attachFileToEntry(attachmentRequestVO));
//
//        // then
//        verify(entryService).getOne(attachmentRequestVO.getEntryID());
//        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
//        verify(attachmentService, never()).attachFileToEntry(any(UploadedFileVO.class), any(EntryVO.class));
//    }
//
//    @Test
//    public void shouldThrowExceptionOnAttachWhenFileDoesNotExist() throws ServiceException {
//
//        // given
//        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.empty());
//
//        // when
//        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.attachFileToEntry(attachmentRequestVO));
//
//        // then
//        verify(entryService, never()).getOne(attachmentRequestVO.getEntryID());
//        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
//        verify(attachmentService, never()).attachFileToEntry(any(UploadedFileVO.class), any(EntryVO.class));
//    }
//
//    @Test
//    public void shouldDetachFileFromEntry() throws ServiceException {
//
//        // given
//        given(entryService.getOne(attachmentRequestVO.getEntryID())).willReturn(entryVO);
//        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));
//
//        // when
//        attachmentFacade.detachFileFromEntry(attachmentRequestVO);
//
//        // then
//        verify(entryService).getOne(attachmentRequestVO.getEntryID());
//        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
//        verify(attachmentService).detachFileFromEntry(uploadedFileVO, entryVO);
//    }
//
//    @Test
//    public void shouldThrowExceptionOnDetachWhenEntryDoesNotExist() throws ServiceException {
//
//        // given
//        doThrow(EntityNotFoundException.class).when(entryService).getOne(attachmentRequestVO.getEntryID());
//        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.of(uploadedFileVO));
//
//        // when
//        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.detachFileFromEntry(attachmentRequestVO));
//
//        // then
//        verify(entryService).getOne(attachmentRequestVO.getEntryID());
//        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
//        verify(attachmentService, never()).detachFileFromEntry(any(UploadedFileVO.class), any(EntryVO.class));
//    }
//
//    @Test
//    public void shouldThrowExceptionOnDetachWhenFileDoesNotExist() throws ServiceException {
//
//        // given
//        given(fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())).willReturn(Optional.empty());
//
//        // when
//        Assertions.assertThrows(ServiceException.class, () -> attachmentFacade.detachFileFromEntry(attachmentRequestVO));
//
//        // then
//        verify(entryService, never()).getOne(attachmentRequestVO.getEntryID());
//        verify(fileManagementFacade).getCheckedMetaInfo(attachmentRequestVO.getPathUUID());
//        verify(attachmentService, never()).detachFileFromEntry(any(UploadedFileVO.class), any(EntryVO.class));
//    }
}
