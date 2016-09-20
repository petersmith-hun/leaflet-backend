package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;

import java.util.List;

/**
 * Comment service operations interface.
 *
 * @author Peter Smith
 */
public interface CommentService extends CreateOperationCapableService<CommentVO, Long>,
        ReadOperationCapableService<CommentVO, Long>,
        UpdateOperationCapableService<CommentVO, CommentVO, Long>,
        DeleteOperationCapableService<CommentVO, Long> {

    /**
     * Returns all comments for given entry.
     *
     * @param entryVO {@link EntryVO} object to return comments for.
     * @return list of all comments under given entry
     */
    public List<CommentVO> getAllCommentsForEntry(EntryVO entryVO);

    /**
     * Returns only enabled comments for given entry.
     *
     * @param entryVO {@link EntryVO} object to return comments for.
     * @return list of enabled comments under given entry
     */
    public List<CommentVO> getEnabledCommentsForEntry(EntryVO entryVO);
}
