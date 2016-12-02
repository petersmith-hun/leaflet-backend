package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.PageableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
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

    /**
     * Returns a page of public entries.
     *
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries
     */
    public EntityPageVO<EntryVO> getPageOfPublicEntries(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy);

    /**
     * Returns a page of public entries under given category.
     *
     * @param categoryVO category to filter entries by
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries under given category
     */
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(CategoryVO categoryVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy);
}
