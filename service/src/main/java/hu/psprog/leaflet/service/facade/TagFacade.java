package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;

import java.util.List;

/**
 * Facade for {@link TagService}.
 *
 * @author Peter Smith
 */
public interface TagFacade {

    /**
     * Returns all enabled (public) tags.
     *
     * @return list of enabled tags
     */
    List<TagVO> getPublicTags();

    /**
     * Returns all existing tags.
     *
     * @return list of tags
     */
    List<TagVO> getAll();

    /**
     * Returns tag identified by given ID.
     *
     * @param id ID of tag to return
     * @return tag data
     * @throws ServiceException if requested tag does not exist
     */
    TagVO getOne(Long id) throws ServiceException;

    /**
     * Returns number of all tags.
     *
     * @return number of all tags
     */
    Long count();

    /**
     * Creates a new tag.
     *
     * @param entity tag data
     * @return created tag data
     * @throws ServiceException if tag could not be created
     */
    TagVO createOne(TagVO entity) throws ServiceException;

    /**
     * Updates an existing tag.
     *
     * @param id ID of tag to update
     * @param updatedEntity tag data
     * @return updated tag data
     * @throws ServiceException if tag could not be updated
     */
    TagVO updateOne(Long id, TagVO updatedEntity) throws ServiceException;

    /**
     * Changes status of tag.
     *
     * @param id ID of tag to change status of
     * @return updated tag data
     * @throws ServiceException if tag could not be updated
     */
    TagVO changeStatus(Long id) throws ServiceException;

    /**
     * Permanently deletes tag identified by given ID.
     *
     * @param id ID of tag to delete
     * @throws ServiceException if tag could not be deleted
     */
    void deletePermanently(Long id) throws ServiceException;

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
}
