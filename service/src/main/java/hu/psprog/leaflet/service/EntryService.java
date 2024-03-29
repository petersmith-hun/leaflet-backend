package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.PageableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;

import java.util.List;

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
    EntryVO findByLink(String link) throws EntityNotFoundException;

    /**
     * Returns a page of public entries.
     *
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries
     */
    EntityPageVO<EntryVO> getPageOfPublicEntries(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy);

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
    EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(CategoryVO categoryVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy);

    /**
     * Returns a page of public entries under given tag.
     *
     * @param tagVO tag to filter entries by
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries under given tag
     */
    EntityPageVO<EntryVO> getPageOfPublicEntriesUnderTag(TagVO tagVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy);

    /**
     * Returns a page of public entries by given content.
     * Content expressions will be looked up in the entry's title, prologue and full content.
     *
     * @param content content expression to filter entries by
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries by given content
     */
    EntityPageVO<EntryVO> getPageOfPublicEntriesByContent(String content, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy);

    /**
     * Returns a paginated list of edit-level entry data for the given search request.
     *
     * @param entrySearchParametersVO {@link EntrySearchParametersVO} object containing search request parameters
     * @return page of entries filtered and paginated by the given search parameters
     */
    EntityPageVO<EntryVO> searchEntries(EntrySearchParametersVO entrySearchParametersVO);

    /**
     * Returns an un-paged list of public entries.
     *
     * @return list of public entries
     */
    List<EntryVO> getListOfPublicEntries();

    /**
     * Changes entry publication status.
     * Publication status is changed in a cycle, in a strict order: entries in draft status can be transitioned to
     * review, then to public status. Public entries can also be transitioned back to draft.
     *
     * @param id ID of entry to change publication status of
     * @param newStatus new publication status to transition entry into
     * @return updated entry data
     * @throws ServiceException if entry cannot be found or status change cannot be performed
     */
    EntryVO changePublicationStatus(Long id, String newStatus) throws ServiceException;
}
