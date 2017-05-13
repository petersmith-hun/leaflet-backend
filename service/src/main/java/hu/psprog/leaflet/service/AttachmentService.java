package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

/**
 * Attachment service operations interface.
 *
 * @author Peter Smith
 */
public interface AttachmentService {

    /**
     * Attaches an existing uploaded file to the given entry.
     *
     * @param uploadedFileVO file to attach to entry
     * @param entryVO entry to attach file to
     */
    void attachFileToEntry(UploadedFileVO uploadedFileVO, EntryVO entryVO) throws EntityNotFoundException;

    /**
     * Detaches an attachment from an entry.
     *
     * @param uploadedFileVO file to detach from entry
     * @param entryVO entry to detach file from
     */
    void detachFileFromEntry(UploadedFileVO uploadedFileVO, EntryVO entryVO) throws EntityNotFoundException;
}
