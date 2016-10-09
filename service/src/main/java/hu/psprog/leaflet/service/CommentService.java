package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;

/**
 * Comment service operations interface.
 *
 * @author Peter Smith
 */
public interface CommentService extends CreateOperationCapableService<CommentVO, Long>,
        ReadOperationCapableService<CommentVO, Long>,
        UpdateOperationCapableService<CommentVO, CommentVO, Long>,
        DeleteOperationCapableService<CommentVO, Long>,
        StatusChangeCapableService<Long> {

    /**
     * Returns all comments for given entry.
     *
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @param entryVO {@link EntryVO} object to return comments for.
     * @return list of all comments under given entry
     */
    public EntityPageVO<CommentVO> getPageOfCommentsForEntry(int page, int limit, OrderDirection direction,
                                                             CommentVO.OrderBy orderBy, EntryVO entryVO);

    /**
     * Returns only enabled comments for given entry.
     *
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @param entryVO {@link EntryVO} object to return comments for.
     * @return list of enabled comments under given entry
     */
    public EntityPageVO<CommentVO> getPageOfPublicCommentsForEntry(int page, int limit, OrderDirection direction,
                                                                   CommentVO.OrderBy orderBy, EntryVO entryVO);

    /**
     * Returns comments created by given user.
     *
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @param userVO {@link UserVO} object to return comments for.
     * @return
     */
    public EntityPageVO<CommentVO> getPageOfCommentsForUser(int page, int limit, OrderDirection direction,
                                                            CommentVO.OrderBy orderBy, UserVO userVO);
}
