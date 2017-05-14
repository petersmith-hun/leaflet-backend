package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.converter.UploadedFileVOToUploadedFileConverter;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link AttachmentService}.
 *
 * @author Peter Smith
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final EntryDAO entryDAO;
    private final UploadedFileDAO uploadedFileDAO;
    private final UploadedFileVOToUploadedFileConverter uploadedFileVOToUploadedFileConverter;

    @Autowired
    public AttachmentServiceImpl(EntryDAO entryDAO, UploadedFileDAO uploadedFileDAO,
                                 UploadedFileVOToUploadedFileConverter uploadedFileVOToUploadedFileConverter) {
        this.entryDAO = entryDAO;
        this.uploadedFileDAO = uploadedFileDAO;
        this.uploadedFileVOToUploadedFileConverter = uploadedFileVOToUploadedFileConverter;

    }

    @Override
    public void attachFileToEntry(UploadedFileVO uploadedFileVO, EntryVO entryVO) throws EntityNotFoundException {

        assertState(uploadedFileVO, entryVO);

        List<UploadedFile> currentAttachments = getCurrentAttachments(entryVO);
        UploadedFile fileToAttach = uploadedFileVOToUploadedFileConverter.convert(uploadedFileVO);
        if (!currentAttachments.contains(fileToAttach)) {
            currentAttachments.add(fileToAttach);
            entryDAO.updateAttachments(entryVO.getId(), currentAttachments);
            LOGGER.info("File [{}] attached to entry [{}].", uploadedFileVO.getPath(), entryVO.getTitle());
        } else {
            LOGGER.warn("File [{}] is already attached to entry [{}].", uploadedFileVO.getPath(), entryVO.getTitle());
        }
    }

    @Override
    public void detachFileFromEntry(UploadedFileVO uploadedFileVO, EntryVO entryVO) throws EntityNotFoundException {

        assertState(uploadedFileVO, entryVO);

        List<UploadedFile> currentAttachments = getCurrentAttachments(entryVO);
        UploadedFile fileToDetach = uploadedFileVOToUploadedFileConverter.convert(uploadedFileVO);
        if (currentAttachments.contains(fileToDetach)) {
            currentAttachments.remove(fileToDetach);
            entryDAO.updateAttachments(entryVO.getId(), currentAttachments);
            LOGGER.info("File [{}] detached from entry [{}].", uploadedFileVO.getPath(), entryVO.getTitle());
        } else {
            LOGGER.warn("File [{}] is not attached to entry [{}].", uploadedFileVO.getPath(), entryVO.getTitle());
        }
    }

    private List<UploadedFile> getCurrentAttachments(EntryVO entryVO) {
        return entryDAO.findOne(entryVO.getId()).getAttachments();
    }

    private void assertState(UploadedFileVO uploadedFileVO, EntryVO entryVO) throws EntityNotFoundException {
        if (!entryDAO.exists(entryVO.getId())) {
            LOGGER.error("Entry identified by [{}] not found", entryVO.getId());
            throw new EntityNotFoundException(EntryVO.class, entryVO.getId());
        }

        if (!uploadedFileDAO.exists(uploadedFileVO.getId())) {
            LOGGER.error("Uploaded file identified by [{}] not found", uploadedFileVO.getId());
            throw new EntityNotFoundException(UploadedFileVO.class, uploadedFileVO.getId());
        }
    }
}
