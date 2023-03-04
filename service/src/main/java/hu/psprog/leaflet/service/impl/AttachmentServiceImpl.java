package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    @Autowired
    public AttachmentServiceImpl(EntryDAO entryDAO) {
        this.entryDAO = entryDAO;
    }

    @Override
    @PermitScope.Write.Entries
    public void attachFileToEntry(UploadedFile uploadedFile, EntryVO entryVO) throws EntityNotFoundException {

        assertState(entryVO);

        List<UploadedFile> currentAttachments = getCurrentAttachments(entryVO);
        if (!isAttached(currentAttachments, uploadedFile)) {
            currentAttachments.add(uploadedFile);
            entryDAO.updateAttachments(entryVO.getId(), currentAttachments);
            LOGGER.info("File [{}] attached to entry [{}].", uploadedFile.getPath(), entryVO.getTitle());
        } else {
            LOGGER.warn("File [{}] is already attached to entry [{}].", uploadedFile.getPath(), entryVO.getTitle());
        }
    }

    @Override
    @PermitScope.Write.Entries
    public void detachFileFromEntry(UploadedFile uploadedFile, EntryVO entryVO) throws EntityNotFoundException {

        assertState(entryVO);

        List<UploadedFile> currentAttachments = getCurrentAttachments(entryVO);
        if (isAttached(currentAttachments, uploadedFile)) {
            currentAttachments.remove(uploadedFile);
            entryDAO.updateAttachments(entryVO.getId(), currentAttachments);
            LOGGER.info("File [{}] detached from entry [{}].", uploadedFile.getPath(), entryVO.getTitle());
        } else {
            LOGGER.warn("File [{}] is not attached to entry [{}].", uploadedFile.getPath(), entryVO.getTitle());
        }
    }

    private List<UploadedFile> getCurrentAttachments(EntryVO entryVO) {

        return entryDAO.findById(entryVO.getId())
                .map(Entry::getAttachments)
                .orElseGet(Collections::emptyList);
    }

    private boolean isAttached(List<UploadedFile> currentAttachments, UploadedFile uploadedFile) {
        return currentAttachments.contains(uploadedFile);
    }

    private void assertState(EntryVO entryVO) throws EntityNotFoundException {

        if (!entryDAO.exists(entryVO.getId())) {
            LOGGER.error("Entry identified by [{}] not found", entryVO.getId());
            throw new EntityNotFoundException(Entry.class, entryVO.getId());
        }
    }
}
