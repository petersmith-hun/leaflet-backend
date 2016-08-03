package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.EntryVO;

/**
 * Entry service operations interface.
 *
 * @author Peter Smith
 */
public interface EntryService extends CreateOperationCapableService<EntryVO, Long>,
        ReadOperationCapableService<EntryVO, Long>,
        UpdateOperationCapableService<EntryVO, EntryVO, Long>,
        DeleteOperationCapableService<EntryVO, Long> {
}
