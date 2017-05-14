package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.AttachmentFacade;
import hu.psprog.leaflet.service.facade.FileManagementFacade;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AttachmentFacade}.
 *
 * @author Peter Smith
 */
@Service
public class AttachmentFacadeImpl implements AttachmentFacade {

    private final EntryService entryService;
    private final FileManagementFacade fileManagementFacade;
    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentFacadeImpl(EntryService entryService, FileManagementFacade fileManagementFacade, AttachmentService attachmentService) {
        this.entryService = entryService;
        this.fileManagementFacade = fileManagementFacade;
        this.attachmentService = attachmentService;
    }

    @Override
    public void attachFileToEntry(AttachmentRequestVO attachmentRequestVO) throws ServiceException {
        UploadedFileVO checkedUploadedFileVO = getUploadedFileVO(attachmentRequestVO);
        EntryVO checkedEntryVO = getEntryVO(attachmentRequestVO);
        attachmentService.attachFileToEntry(checkedUploadedFileVO, checkedEntryVO);
    }

    @Override
    public void detachFileFromEntry(AttachmentRequestVO attachmentRequestVO) throws ServiceException {
        UploadedFileVO checkedUploadedFileVO = getUploadedFileVO(attachmentRequestVO);
        EntryVO checkedEntryVO = getEntryVO(attachmentRequestVO);
        attachmentService.detachFileFromEntry(checkedUploadedFileVO, checkedEntryVO);
    }

    private EntryVO getEntryVO(AttachmentRequestVO attachmentRequestVO) throws ServiceException {
        return entryService.getOne(attachmentRequestVO.getEntryID());
    }

    private UploadedFileVO getUploadedFileVO(AttachmentRequestVO attachmentRequestVO) throws ServiceException {
        return fileManagementFacade.getCheckedMetaInfo(attachmentRequestVO.getPathUUID())
                .orElseThrow(() -> new EntityNotFoundException(UploadedFileVO.class, attachmentRequestVO.getPathUUID()));
    }
}
