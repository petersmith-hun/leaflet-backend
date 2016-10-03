package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.PageableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.AttachmentVO;
import hu.psprog.leaflet.service.vo.EntryVO;

import java.util.List;

/**
 * Attachment service operations interface.
 *
 * @author Peter Smith
 */
public interface AttachmentService extends CreateOperationCapableService<AttachmentVO, Long>,
        ReadOperationCapableService<AttachmentVO, Long>,
        UpdateOperationCapableService<AttachmentVO, AttachmentVO, Long>,
        DeleteOperationCapableService<AttachmentVO, Long>,
        StatusChangeCapableService<Long>,
        PageableService<AttachmentVO, AttachmentVO.OrderBy> {

    /**
     * Returns attachments for given {@link EntryVO}.
     *
     * @param entryVO owner entry
     * @return list of attachments attached to given entry
     */
    public List<AttachmentVO> findByEntry(EntryVO entryVO);

    /**
     * Returns only public (enabled) attachments for given {@link EntryVO}.
     *
     * @param entryVO owner entry
     * @return list of public attachments attached to given entry
     */
    public List<AttachmentVO> getPublicAttachmentsForEntry(EntryVO entryVO);
}
