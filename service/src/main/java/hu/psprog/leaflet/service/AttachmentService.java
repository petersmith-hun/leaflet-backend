package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.AttachmentVO;

/**
 * Attachment service operations interface.
 *
 * @author Peter Smith
 */
public interface AttachmentService extends CreateOperationCapableService<AttachmentVO, Long>,
        ReadOperationCapableService<AttachmentVO, Long>,
        UpdateOperationCapableService<AttachmentVO, AttachmentVO, Long>,
        DeleteOperationCapableService<AttachmentVO, Long> {
}
