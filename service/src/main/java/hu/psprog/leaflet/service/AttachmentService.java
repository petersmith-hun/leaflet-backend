package hu.psprog.leaflet.service;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.vo.EntryVO;

/**
 * Attachment service operations interface.
 *
 * @author Peter Smith
 */
public interface AttachmentService {

    /**
     * Attaches an existing uploaded file to the given entry.
     *
     * @param uploadedFile file to attach to entry
     * @param entryVO entry to attach file to
     */
    void attachFileToEntry(UploadedFile uploadedFile, EntryVO entryVO) throws EntityNotFoundException;

    /**
     * Detaches an attachment from an entry.
     *
     * @param uploadedFile file to detach from entry
     * @param entryVO entry to detach file from
     */
    void detachFileFromEntry(UploadedFile uploadedFile, EntryVO entryVO) throws EntityNotFoundException;
}
