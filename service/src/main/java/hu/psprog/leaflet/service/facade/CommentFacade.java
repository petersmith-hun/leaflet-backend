package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;

import java.util.List;

/**
 * Facade for {@link CommentService}.
 *
 * @author Peter Smith
 */
public interface CommentFacade {

    /**
     * Returns all comments for given entry.
     *
     * @param entryID ID of entry to return comments for.
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return list of all comments under given entry
     */
    EntityPageVO<CommentVO> getPageOfCommentsForEntry(Long entryID, int page, int limit, String direction, String orderBy);

    /**
     * Returns only enabled comments for given entry.
     *
     * @param entryLink link of entry to return comments for.
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return list of enabled comments under given entry
     */
    EntityPageVO<CommentVO> getPageOfPublicCommentsForEntry(String entryLink, int page, int limit, String direction, String orderBy);

    /**
     * Returns comments created by given user.
     *
     * @param userID ID of user to return comments for.
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return paged list of comments created by given user
     */
    EntityPageVO<CommentVO> getPageOfCommentsForUser(Long userID, int page, int limit, String direction, String orderBy);

    /**
     * Passes comment for persistence layer and returns ID of newly created comment.
     *
     * @param entity {@link CommentVO} value object
     * @return ID of newly created comment
     */
    Long createOne(CommentVO entity) throws ServiceException;

    /**
     * Retrieves entity of type {@link CommentVO} specified by ID.
     *
     * @param id ID of the comment
     * @return comment identified by given identifier
     */
    CommentVO getOne(Long id) throws ServiceException;

    /**
     * Retrieves all entity of type {@link CommentVO}.
     *
     * @return list of all entities of type {@link CommentVO}
     */
    List<CommentVO> getAll();

    /**
     * Updates comment specified by given ID. Returns updated comment.
     *
     * @param id ID of comment
     * @param updatedComment updated {@link CommentVO}
     * @return updated comment
     */
    CommentVO updateOne(Long id, CommentVO updatedComment) throws ServiceException;

    /**
     * Logically deletes given comment.
     *
     * @param id ID of the comment to delete
     */
    void deleteLogically(Long id) throws ServiceException;

    /**
     * Deletes comment by its identifier.
     *
     * @param id ID of the comment to delete
     */
    void deletePermanently(Long id) throws ServiceException;

    /**
     * Restores comment by reverting logical deletion.
     *
     * @param id ID of the comment to delete
     */
    void restoreEntity(Long id) throws ServiceException;

    /**
     * Changes comment status.
     * If comment is currently enabled, status will be updated to disabled and backwards.
     *
     * @param id ID of comment to change status of
     * @return updated comment data
     * @throws ServiceException if comment cannot be found or status change cannot be performed
     */
    CommentVO changeStatus(Long id) throws ServiceException;
}
