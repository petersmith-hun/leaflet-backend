package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.TagFacade;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<TagVO> getPublicTags() {
        return tagService.getPublicTags();
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
    public TagVO createOne(TagVO entity) throws ServiceException {
        Long createdID = tagService.createOne(entity);
        return tagService.getOne(createdID);
    }

    @Override
    public TagVO updateOne(Long id, TagVO updatedEntity) throws ServiceException {
        tagService.updateOne(id, updatedEntity);
        return tagService.getOne(id);
    }

    @Override
    public TagVO changeStatus(Long id) throws ServiceException {

        TagVO tagVO = tagService.getOne(id);
        if (tagVO.isEnabled()) {
            tagService.disable(id);
        } else {
            tagService.enable(id);
        }

        return tagService.getOne(id);
    }

    @Override
    public void deletePermanently(Long id) throws ServiceException {
        tagService.deleteByID(id);
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

    private EntryVO getEntryVO(TagAssignmentVO tagAssignmentVO) throws ServiceException {
        return entryService.getOne(tagAssignmentVO.getEntryID());
    }

    private TagVO getTagVO(TagAssignmentVO tagAssignmentVO) throws ServiceException {
        return tagService.getOne(tagAssignmentVO.getTagID());
    }
}
