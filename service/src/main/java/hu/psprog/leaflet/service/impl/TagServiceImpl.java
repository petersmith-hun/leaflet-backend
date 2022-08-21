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
import hu.psprog.leaflet.service.security.annotation.PermitScope;
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

    private final TagDAO tagDAO;
    private final EntryDAO entryDAO;
    private final TagToTagVOConverter tagToTagVOConverter;
    private final TagVOToTagConverter tagVOToTagConverter;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, EntryDAO entryDAO, TagToTagVOConverter tagToTagVOConverter, TagVOToTagConverter tagVOToTagConverter) {
        this.tagDAO = tagDAO;
        this.entryDAO = entryDAO;
        this.tagToTagVOConverter = tagToTagVOConverter;
        this.tagVOToTagConverter = tagVOToTagConverter;
    }

    @Override
    @PermitScope.Read.Tags
    public TagVO getOne(Long id) throws ServiceException {

        Tag tag = tagDAO.findOne(id);

        if (tag == null) {
            throw new EntityNotFoundException(Tag.class, id);
        }
        
        return tagToTagVOConverter.convert(tag);
    }

    @Override
    @PermitScope.Read.Tags
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
    @PermitScope.Read.Tags
    public Long count() {

        return tagDAO.count();
    }

    @Override
    @PermitScope.Write.Tags
    public Long createOne(TagVO entity) throws ServiceException {

        Tag tag = tagVOToTagConverter.convert(entity);
        Tag savedTag = tagDAO.save(tag);

        if (savedTag == null) {
            throw new EntityCreationException(Tag.class);
        }

        LOGGER.info("New tag [{}] has been created with ID [{}]", savedTag.getTitle(), savedTag.getId());

        return savedTag.getId();
    }

    @Override
    @PermitScope.Write.Tags
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
    @PermitScope.Write.Tags
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
    @PermitScope.Write.Tags
    public TagVO updateOne(Long id, TagVO updatedEntity) throws ServiceException {

        Tag updatedTag = tagDAO.updateOne(id, tagVOToTagConverter.convert(updatedEntity));

        if (updatedTag == null) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        LOGGER.info("Existing tag [{}] with ID [{}] has been updated", updatedTag.getTitle(), id);

        return tagToTagVOConverter.convert(updatedTag);
    }

    @Override
    @PermitScope.Write.Tags
    public void deleteByID(Long id) throws ServiceException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.delete(id);
        LOGGER.info("Deleted tag of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Tags
    public void enable(Long id) throws EntityNotFoundException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.enable(id);
        LOGGER.info("Enabled tag of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Tags
    public void disable(Long id) throws EntityNotFoundException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.disable(id);
        LOGGER.info("Disabled tag of ID [{}]", id);
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
