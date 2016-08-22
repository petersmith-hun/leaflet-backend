package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.DocumentVO;

/**
 * Document service operations interface.
 *
 * @author Peter Smith
 */
public interface DocumentService extends CreateOperationCapableService<DocumentVO, Long>,
        ReadOperationCapableService<DocumentVO, Long>,
        UpdateOperationCapableService<DocumentVO, DocumentVO, Long>,
        DeleteOperationCapableService<DocumentVO, Long> {
}
