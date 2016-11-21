package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.AttachmentDAO;
import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.persistence.repository.specification.AttachmentSpecification;
import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.AttachmentToAttachmentVOConverter;
import hu.psprog.leaflet.service.converter.AttachmentVOToAttachmentConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.AttachmentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AttachmentService}.
 *
 * @author Peter Smith
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentDAO attachmentDAO;

    @Autowired
    private AttachmentToAttachmentVOConverter attachmentToAttachmentVOConverter;

    @Autowired
    private AttachmentVOToAttachmentConverter attachmentVOToAttachmentConverter;

    @Autowired
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Override
    public AttachmentVO getOne(Long id) throws ServiceException {

        Attachment attachment = attachmentDAO.findOne(id);

        if (attachment == null) {
            throw new EntityNotFoundException(Attachment.class, id);
        }

        return attachmentToAttachmentVOConverter.convert(attachment);
    }

    @Override
    public List<AttachmentVO> getAll() {

        return attachmentDAO.findAll().stream()
                .map(attachment -> attachmentToAttachmentVOConverter.convert(attachment))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {

        return attachmentDAO.count();
    }

    @Override
    public List<AttachmentVO> findByEntry(EntryVO entryVO) {

        return attachmentDAO.findByEntry(entryVOToEntryConverter.convert(entryVO)).stream()
                .map(attachment -> attachmentToAttachmentVOConverter.convert(attachment))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttachmentVO> getPublicAttachmentsForEntry(EntryVO entryVO) {

        return attachmentDAO.findByEntry(AttachmentSpecification.isEnabled, entryVOToEntryConverter.convert(entryVO)).stream()
                .map(attachment -> attachmentToAttachmentVOConverter.convert(attachment))
                .collect(Collectors.toList());
    }

    @Override
    public EntityPageVO<AttachmentVO> getEntityPage(int page, int limit, OrderDirection direction, AttachmentVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<Attachment> entityPage = attachmentDAO.findAll(pageable);

        return PageableUtil.convertPage(entityPage, attachmentToAttachmentVOConverter);
    }

    @Override
    public Long createOne(AttachmentVO entity) throws ServiceException {

        Attachment attachment = attachmentVOToAttachmentConverter.convert(entity);
        Attachment savedAttachment;
        try {
            savedAttachment = attachmentDAO.save(attachment);
        } catch (PersistenceException e) {
            throw new ConstraintViolationException(e);
        }

        if (savedAttachment == null) {
            throw new EntityCreationException(Attachment.class);
        }

        return savedAttachment.getId();
    }

    @Override
    public List<Long> createBulk(List<AttachmentVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (AttachmentVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    public AttachmentVO updateOne(Long id, AttachmentVO updatedEntity) throws ServiceException {

        Attachment updatedAttachment;
        try {
            updatedAttachment = attachmentDAO.updateOne(id, attachmentVOToAttachmentConverter.convert(updatedEntity));
        } catch (PersistenceException e) {
            throw new ConstraintViolationException(e);
        }

        if (updatedAttachment == null) {
            throw new EntityNotFoundException(Attachment.class, id);
        }

        return attachmentToAttachmentVOConverter.convert(updatedAttachment);
    }

    @Override
    public List<AttachmentVO> updateBulk(Map<Long, AttachmentVO> updatedEntities) throws ServiceException {

        List<AttachmentVO> attachmentVOs = new LinkedList<>();

        Iterator<Map.Entry<Long, AttachmentVO>> entities = updatedEntities.entrySet().iterator();
        while (entities.hasNext()) {
            Map.Entry<Long, AttachmentVO> currentEntity = entities.next();
            AttachmentVO updatedEntity = updateOne(currentEntity.getKey(), currentEntity.getValue());
            attachmentVOs.add(updatedEntity);
        }

        return attachmentVOs;
    }

    @Override
    public void deleteByEntity(AttachmentVO entity) throws ServiceException {

        if (!attachmentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Attachment.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {

        try {
            attachmentDAO.delete(id);
        } catch (IllegalArgumentException exc){
            throw new EntityNotFoundException(Attachment.class, id);
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

        if (!attachmentDAO.exists(id)) {
            throw new EntityNotFoundException(Attachment.class, id);
        }

        attachmentDAO.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!attachmentDAO.exists(id)) {
            throw new EntityNotFoundException(Attachment.class, id);
        }

        attachmentDAO.disable(id);
    }
}
