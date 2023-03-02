package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;

import java.util.List;

/**
 * Facade for {@link EntryService}.
 *
 * @author Peter Smith
 */
public interface EntryFacade {

    /**
     * Returns {@link EntryVO} related to given link.
     *
     * @param link link to retrieve entry by
     * @return entry related to given link or null if not exists
     * @throws EntityNotFoundException when entity identified by given link does not exist
     */
    EntryVO findByLink(String link) throws EntityNotFoundException;

    /**
     * Returns a page of entries.
     *
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of entries
     */
    EntityPageVO<EntryVO> getEntityPage(int page, int limit, String direction, String orderBy);

    /**
     * Returns a page of public entries.
     *
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries
     */
    EntityPageVO<EntryVO> getPageOfPublicEntries(int page, int limit, String direction, String orderBy);

    /**
     * Returns an un-paged list of public entries.
     *
     * @return list of public entries
     */
    List<EntryVO> getListOfPublicEntries();

    /**
     * Returns a page of public entries under given category.
     *
     * @param categoryID category ID to filter entries by
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries under given category
     */
    EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(Long categoryID, int page, int limit, String direction, String orderBy);

    /**
     * Returns a page of public entries under given tag.
     *
     * @param tagID tag ID to filter entries by
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries under given tag
     */
    EntityPageVO<EntryVO> getPageOfPublicEntriesUnderTag(Long tagID, int page, int limit, String direction, String orderBy);

    /**
     * Returns a page of public entries by given content.
     * Content expressions will be looked up in entry's title, prologue and full content.
     *
     * @param content content expression to filter entries by
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public entries by given content expression
     */
    EntityPageVO<EntryVO> getPageOfPublicEntriesByContent(String content, int page, int limit, String direction, String orderBy);

    /**
     * Passes entry for persistence layer and returns ID of newly created entry.
     *
     * @param entity {@link EntryVO} value object
     * @return created entry data
     */
    EntryVO createOne(EntryVO entity) throws ServiceException;

    /**
     * Retrieves entity of type {@link EntryVO} specified by ID.
     *
     * @param id ID of the entry
     * @return entry identified by given identifier
     */
    EntryVO getOne(Long id) throws ServiceException;

    /**
     * Retrieves all entity of type {@link EntryVO}.
     *
     * @return list of all entities of type {@link EntryVO}
     */
    List<EntryVO> getAll();

    /**
     * Updates entry specified by given ID. Returns updated entry.
     *
     * @param id ID of entry
     * @param updatedEntry updated {@link EntryVO}
     * @return updated entry
     */
    EntryVO updateOne(Long id, EntryVO updatedEntry) throws ServiceException;

    /**
     * Deletes entry by its identifier.
     *
     * @param id ID of the entry to delete
     */
    void deletePermanently(Long id) throws ServiceException;

    /**
     * Changes entry status.
     * If entry is currently enabled, status will be updated to disabled and backwards.
     *
     * @param id ID of entry to change status of
     * @return updated entry data
     * @throws ServiceException if entry cannot be found or status change cannot be performed
     */
    EntryVO changeStatus(Long id) throws ServiceException;
}
