package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.CommentVO;

/**
 * Comment service operations interface.
 *
 * @author Peter Smith
 */
public interface CommentService extends CreateOperationCapableService<CommentVO, Long>,
        ReadOperationCapableService<CommentVO, Long>,
        UpdateOperationCapableService<CommentVO, CommentVO, Long>,
        DeleteOperationCapableService<CommentVO, Long> {
}
