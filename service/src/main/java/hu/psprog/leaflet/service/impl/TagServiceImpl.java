package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.TagDAO;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.repository.specification.TagSpecification;
import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.converter.TagToTagVOConverter;
import hu.psprog.leaflet.service.converter.TagVOToTagConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TagService}.
 *
 * @author Peter Smith
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDAO tagDAO;
    
    @Autowired
    private TagToTagVOConverter tagToTagVOConverter;
    
    @Autowired
    private TagVOToTagConverter tagVOToTagConverter;
    
    @Override
    public TagVO getOne(Long id) throws ServiceException {

        Tag tag = tagDAO.findOne(id);

        if (tag == null) {
            throw new EntityNotFoundException(Tag.class, id);
        }
        
        return tagToTagVOConverter.convert(tag);
    }

    @Override
    public List<TagVO> getAll() {

        return tagDAO.findAll().stream()
                .map(tag -> tagToTagVOConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagVO> getPublicTags() {

        return tagDAO.findAll(TagSpecification.isEnabled).stream()
                .map(tag -> tagToTagVOConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {

        return tagDAO.count();
    }

    @Override
    public Long createOne(TagVO entity) throws ServiceException {

        Tag tag = tagVOToTagConverter.convert(entity);
        Tag savedTag = tagDAO.save(tag);

        if (savedTag == null) {
            throw new EntityCreationException(Tag.class);
        }

        return savedTag.getId();
    }

    @Override
    public List<Long> createBulk(List<TagVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (TagVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    public void connectTagToEntry(TagVO tagVO, EntryVO entryVO) {

        // TODO implement method later
    }

    @Override
    public TagVO updateOne(Long id, TagVO updatedEntity) throws ServiceException {

        Tag updatedTag = tagDAO.updateOne(id, tagVOToTagConverter.convert(updatedEntity));

        if (updatedTag == null) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        return tagToTagVOConverter.convert(updatedTag);
    }

    @Override
    public List<TagVO> updateBulk(Map<Long, TagVO> updatedEntities) throws ServiceException {

        List<TagVO> tagVOs = new LinkedList<>();

        Iterator<Map.Entry<Long, TagVO>> entities = updatedEntities.entrySet().iterator();
        while (entities.hasNext()) {
            Map.Entry<Long, TagVO> currentEntity = entities.next();
            TagVO updatedEntity = updateOne(currentEntity.getKey(), currentEntity.getValue());
            tagVOs.add(updatedEntity);
        }

        return tagVOs;
    }

    @Override
    public void deleteByEntity(TagVO entity) throws ServiceException {

        if (!tagDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Tag.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {

        try {
            tagDAO.delete(id);
        } catch (IllegalArgumentException exc){
            throw new EntityNotFoundException(Tag.class, id);
        }
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!tagDAO.exists(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }

        tagDAO.disable(id);
    }
}
