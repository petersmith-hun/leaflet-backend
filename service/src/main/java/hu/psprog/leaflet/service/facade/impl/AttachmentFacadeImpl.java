package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.AttachmentFacade;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AttachmentFacade}.
 *
 * @author Peter Smith
 */
@Service
@Slf4j
public class AttachmentFacadeImpl implements AttachmentFacade {

    private final EntryService entryService;
    private final AttachmentService attachmentService;
    private final UploadedFileDAO uploadedFileDAO;

    @Autowired
    public AttachmentFacadeImpl(EntryService entryService, AttachmentService attachmentService,
                                UploadedFileDAO uploadedFileDAO) {
        this.entryService = entryService;
        this.attachmentService = attachmentService;
        this.uploadedFileDAO = uploadedFileDAO;
    }

    @Override
    public void attachFileToEntry(AttachmentRequestVO attachmentRequestVO) throws ServiceException {

        UploadedFile uploadedFile = getUploadedFile(attachmentRequestVO);
        EntryVO checkedEntryVO = getEntryVO(attachmentRequestVO);
        attachmentService.attachFileToEntry(uploadedFile, checkedEntryVO);
    }

    @Override
    public void detachFileFromEntry(AttachmentRequestVO attachmentRequestVO) throws ServiceException {

        UploadedFile uploadedFile = getUploadedFile(attachmentRequestVO);
        EntryVO checkedEntryVO = getEntryVO(attachmentRequestVO);
        attachmentService.detachFileFromEntry(uploadedFile, checkedEntryVO);
    }

    private EntryVO getEntryVO(AttachmentRequestVO attachmentRequestVO) throws ServiceException {
        return entryService.getOne(attachmentRequestVO.getEntryID());
    }

    private UploadedFile getUploadedFile(AttachmentRequestVO attachmentRequestVO) throws ServiceException {

        return uploadedFileDAO.findByPathUUID(attachmentRequestVO.getPathUUID())
                .orElseThrow(() -> new EntityNotFoundException(UploadedFile.class, attachmentRequestVO.getPathUUID()));
    }
}
