package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;

import java.util.List;

/**
 * Tag service operations interface.
 *
 * @author Peter Smith
 */
public interface TagService extends CreateOperationCapableService<TagVO, Long>,
        ReadOperationCapableService<TagVO, Long>,
        UpdateOperationCapableService<TagVO, TagVO, Long>,
        DeleteOperationCapableService<TagVO, Long>,
        StatusChangeCapableService<Long> {

    /**
     * Attaches an existing tag to an existing entry.
     *
     * @param tagVO {@link TagVO} object
     * @param entryVO {@link EntryVO} object
     */
    void attachTagToEntry(TagVO tagVO, EntryVO entryVO) throws EntityNotFoundException;

    /**
     * Detaches an existing tag from an existing entry.
     *
     * @param tagVO {@link TagVO} object
     * @param entryVO {@link EntryVO} object
     */
    void detachTagFromEntry(TagVO tagVO, EntryVO entryVO) throws EntityNotFoundException;

    /**
     * Returns all enabled (public) tags.
     *
     * @return list of enabled tags
     */
    List<TagVO> getPublicTags();
}
