package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;

import java.util.List;

/**
 * Facade for {@link TagService}.
 *
 * @author Peter Smith
 */
public interface TagFacade extends CreateOperationCapableService<TagVO, Long>,
        ReadOperationCapableService<TagVO, Long>,
        UpdateOperationCapableService<TagVO, TagVO, Long>,
        DeleteOperationCapableService<TagVO, Long>,
        StatusChangeCapableService<Long> {

    /**
     * Attaches an existing tag to an existing entry.
     *
     * @param tagAssignmentVO {@link TagAssignmentVO} containing the ID of an entry and a tag to assign to each other
     */
    void attachTagToEntry(TagAssignmentVO tagAssignmentVO) throws ServiceException;

    /**
     * Detaches an existing tag from an existing entry.
     *
     * @param tagAssignmentVO {@link TagAssignmentVO} containing the ID of an entry and a tag to assign to each other
     */
    void detachTagFromEntry(TagAssignmentVO tagAssignmentVO) throws ServiceException;

    /**
     * Returns all enabled (public) tags.
     *
     * @return list of enabled tags
     */
    List<TagVO> getPublicTags();
}
