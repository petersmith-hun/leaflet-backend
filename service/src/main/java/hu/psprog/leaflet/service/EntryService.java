package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.PageableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.vo.EntryVO;

/**
 * Entry service operations interface.
 *
 * @author Peter Smith
 */
public interface EntryService extends CreateOperationCapableService<EntryVO, Long>,
        ReadOperationCapableService<EntryVO, Long>,
        UpdateOperationCapableService<EntryVO, EntryVO, Long>,
        DeleteOperationCapableService<EntryVO, Long>,
        PageableService<EntryVO, EntryVO.OrderBy>,
        StatusChangeCapableService<Long> {

    /**
     * Returns {@link EntryVO} related to given link.
     *
     * @param link link to retrieve entry by
     * @return entry related to given link or null if not exists
     * @throws EntityNotFoundException when entity identified by given link does not exist
     */
    public EntryVO findByLink(String link) throws EntityNotFoundException;
}
