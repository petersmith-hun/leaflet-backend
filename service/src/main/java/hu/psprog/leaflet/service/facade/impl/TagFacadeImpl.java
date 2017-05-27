package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.TagFacade;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link TagFacade}.
 *
 * @author Peter Smith
 */
@Service
public class TagFacadeImpl implements TagFacade {

    private TagService tagService;
    private EntryService entryService;

    @Autowired
    public TagFacadeImpl(TagService tagService, EntryService entryService) {
        this.tagService = tagService;
        this.entryService = entryService;
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {
        tagService.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {
        tagService.disable(id);
    }

    @Override
    public void deleteByEntity(TagVO entity) throws ServiceException {
        tagService.deleteByEntity(entity);
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {
        tagService.deleteByID(id);
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {
        tagService.deleteBulkByIDs(ids);
    }

    @Override
    public TagVO getOne(Long id) throws ServiceException {
        return tagService.getOne(id);
    }

    @Override
    public List<TagVO> getAll() {
        return tagService.getAll();
    }

    @Override
    public Long count() {
        return tagService.count();
    }

    @Override
    public Long createOne(TagVO entity) throws ServiceException {
        return tagService.createOne(entity);
    }

    @Override
    public List<Long> createBulk(List<TagVO> entities) throws ServiceException {
        return tagService.createBulk(entities);
    }

    @Override
    public TagVO updateOne(Long id, TagVO updatedEntity) throws ServiceException {
        return tagService.updateOne(id, updatedEntity);
    }

    @Override
    public List<TagVO> updateBulk(Map<Long, TagVO> updatedEntities) throws ServiceException {
        return tagService.updateBulk(updatedEntities);
    }

    @Override
    public void attachTagToEntry(TagAssignmentVO tagAssignmentVO) throws ServiceException {
        TagVO tagVO = getTagVO(tagAssignmentVO);
        EntryVO entryVO = getEntryVO(tagAssignmentVO);
        tagService.attachTagToEntry(tagVO, entryVO);
    }

    @Override
    public void detachTagFromEntry(TagAssignmentVO tagAssignmentVO) throws ServiceException {
        TagVO tagVO = getTagVO(tagAssignmentVO);
        EntryVO entryVO = getEntryVO(tagAssignmentVO);
        tagService.detachTagFromEntry(tagVO, entryVO);
    }

    @Override
    public List<TagVO> getPublicTags() {
        return tagService.getPublicTags();
    }

    private EntryVO getEntryVO(TagAssignmentVO tagAssignmentVO) throws ServiceException {
        return entryService.getOne(tagAssignmentVO.getEntryID());
    }

    private TagVO getTagVO(TagAssignmentVO tagAssignmentVO) throws ServiceException {
        return tagService.getOne(tagAssignmentVO.getTagID());
    }
}
