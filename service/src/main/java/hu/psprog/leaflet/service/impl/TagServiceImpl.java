package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.dao.TagDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.repository.specification.TagSpecification;
import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.converter.TagToTagVOConverter;
import hu.psprog.leaflet.service.converter.TagVOToTagConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitEditorOrAdmin;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TagService}.
 *
 * @author Peter Smith
 */
@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);

    private TagDAO tagDAO;
    private EntryDAO entryDAO;
    private TagToTagVOConverter tagToTagVOConverter;
    private TagVOToTagConverter tagVOToTagConverter;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, EntryDAO entryDAO, TagToTagVOConverter tagToTagVOConverter, TagVOToTagConverter tagVOToTagConverter) {
        this.tagDAO = tagDAO;
        this.entryDAO = entryDAO;
        this.tagToTagVOConverter = tagToTagVOConverter;
        this.tagVOToTagConverter = tagVOToTagConverter;
    }

    @Override
    @PermitEditorOrAdmin
    public TagVO getOne(Long id) throws ServiceException {

        Tag tag = tagDAO.findOne(id);

        if (tag == null) {
            throw new EntityNotFoundException(Tag.class, id);
        }
        
        return tagToTagVOConverter.convert(tag);
    }

    @Override
    @PermitEditorOrAdmin
    public List<TagVO> getAll() {

        return tagDAO.findAll().stream()
                .map(tagToTagVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagVO> getPublicTags() {

        return tagDAO.findAll(TagSpecification.IS_ENABLED).stream()
                .map(tagToTagVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitEditorOrAdmin
    public Long count() {

        return tagDAO.count();
    }

    @Override
    @PermitEditorOrAdmin
    public Long createOne(TagVO entity) throws ServiceException {

        Tag tag = tagVOToTagConverter.convert(entity);
        Tag savedTag = tagDAO.save(tag);

        if (savedTag == null) {
            throw new EntityCreationException(Tag.class);
        }

        return savedTag.getId();
    }

    @Override
    @PermitEditorOrAdmin
    public void attachTagToEntry(TagVO tagVO, EntryVO entryVO) throws ServiceException {

        assertState(tagVO, entryVO);

        List<Tag> currentTags = getCurrentTags(entryVO);
        Tag tagToAttach = tagVOToTagConverter.convert(tagVO);
        if (!currentTags.contains(tagToAttach)) {
            currentTags.add(tagToAttach);
            entryDAO.updateTags(entryVO.getId(), currentTags);
            LOGGER.info("Tag [{}] attached to entry [{}]", tagVO.getTitle(), entryVO.getTitle());
        } else {
            LOGGER.warn("Tag [{}] is already attached to entry [{}]", tagVO.getTitle(), entryVO.getTitle());
        }
    }

    @Override
    @PermitEditorOrAdmin
    public void detachTagFromEntry(TagVO tagVO, EntryVO entryVO) throws ServiceException {

        assertState(tagVO, entryVO);

        List<Tag> currentTags = getCurrentTags(entryVO);
        Tag tagToDetach = tagVOToTagConverter.convert(tagVO);
        if (currentTags.contains(tagToDetach)) {
            currentTags.remove(tagToDetach);
            entryDAO.updateTags(entryVO.getId(), currentTags);
            LOGGER.info("Tag [{}] detached from entry [{}]", tagVO.getTitle(), entryVO.getTitle());
        } else {
            LOGGER.warn("Tag [{}] is not attached to entry [{}]", tagVO.getTitle(), entryVO.getTitle());
        }
    }

    @Override
    @PermitEditorOrAdmin
    public TagVO updateOne(Long id, TagVO updatedEntity) throws ServiceException {

        Tag updatedTag = tagDAO.updateOne(id, tagVOToTagConverter.convert(updatedEntity));

        if (updatedTag == null) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        return tagToTagVOConverter.convert(updatedTag);
    }

    @Override
    @PermitEditorOrAdmin
    public void deleteByID(Long id) throws ServiceException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.delete(id);
    }

    @Override
    @PermitEditorOrAdmin
    public void enable(Long id) throws EntityNotFoundException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.enable(id);
    }

    @Override
    @PermitEditorOrAdmin
    public void disable(Long id) throws EntityNotFoundException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.disable(id);
    }

    private List<Tag> getCurrentTags(EntryVO entryVO) {
        return entryDAO.findOne(entryVO.getId()).getTags();
    }

    private void assertState(TagVO tagVO, EntryVO entryVO) throws EntityNotFoundException {
        if (!entryDAO.exists(entryVO.getId())) {
            LOGGER.error("Entry identified by [{}] not found", entryVO.getId());
            throw new EntityNotFoundException(Entry.class, entryVO.getId());
        }

        if (!tagDAO.exists(tagVO.getId())) {
            LOGGER.error("Tag identified by [{}] not found", tagVO.getId());
            throw new EntityNotFoundException(Tag.class, tagVO.getId());
        }
    }
}
