package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.AttachmentDAO;
import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.repository.AttachmentRepository;
import hu.psprog.leaflet.persistence.repository.specification.AttachmentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * DAO implementation for {@link AttachmentRepository}.
 *
 * @author Peter Smith
 */
@Component
public class AttachmentDAOImpl extends SelfStatusAwareDAOImpl<Attachment, Long> implements AttachmentDAO {

    @Autowired
    public AttachmentDAOImpl(final AttachmentRepository attachmentRepository) {
        super(attachmentRepository);
    }

    @Override
    public List<Attachment> findByEntry(Entry entry) {
        return ((AttachmentRepository) jpaRepository).findByEntry(entry);
    }

    @Override
    public List<Attachment> findByEntry(Specification<Attachment> specification, Entry entry) {
        return ((AttachmentRepository) jpaRepository).findAll(Specifications.where(specification)
                .and(AttachmentSpecification.isOwnedByEntry(entry)));
    }

    @Transactional
    @Override
    public Attachment updateOne(Long id, Attachment updatedEntity) {

        Attachment currentAttachment = jpaRepository.getOne(id);
        if (currentAttachment != null) {
            currentAttachment.setTitle(updatedEntity.getTitle());
            currentAttachment.setDescription(updatedEntity.getDescription());
            currentAttachment.setLastModified(new Date());
        }

        return currentAttachment;
    }
}
