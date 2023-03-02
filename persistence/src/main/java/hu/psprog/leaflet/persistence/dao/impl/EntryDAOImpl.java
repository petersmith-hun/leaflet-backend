package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.persistence.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DAO implementation for {@link EntryRepository}.
 *
 * @author Peter Smith
 */
@Component
public class EntryDAOImpl extends SelfStatusAwareDAOImpl<Entry, Long> implements EntryDAO {

    @Autowired
    public EntryDAOImpl(final EntryRepository entryRepository, JpaContext jpaContext) {
        super(entryRepository, jpaContext);
    }

    @Override
    public Optional<Entry> findByLink(String link) {
        return ((EntryRepository) jpaRepository).findByLink(link);
    }

    @Override
    public Page<Entry> findAll(Specification<Entry> specification, Pageable pageable) {
        return ((EntryRepository) jpaRepository).findAll(specification, pageable);
    }

    @Transactional
    @Override
    public void updateAttachments(Long id, List<UploadedFile> attachments) {

        findEntry(id).ifPresent(currentEntry -> {
            currentEntry.setAttachments(attachments);
            jpaRepository.flush();
        });
    }

    @Transactional
    @Override
    public void updateTags(Long id, List<Tag> tags) {

        findEntry(id).ifPresent(currentEntry -> {
            currentEntry.setTags(tags);
            jpaRepository.flush();
        });
    }

    @Override
    protected void doUpdate(Entry currentEntity, Entry updatedEntity) {

        currentEntity.setTitle(updatedEntity.getTitle());
        currentEntity.setRawContent(updatedEntity.getRawContent());
        currentEntity.setPrologue(updatedEntity.getPrologue());
        currentEntity.setSeoTitle(updatedEntity.getSeoTitle());
        currentEntity.setSeoDescription(updatedEntity.getSeoDescription());
        currentEntity.setSeoKeywords(updatedEntity.getSeoKeywords());
        currentEntity.setLink(updatedEntity.getLink());
        currentEntity.setCategory(updatedEntity.getCategory());
        currentEntity.setStatus(updatedEntity.getStatus());
        currentEntity.setEnabled(updatedEntity.isEnabled());
        currentEntity.setLocale(updatedEntity.getLocale());
        currentEntity.setPublished(updatedEntity.getPublished());
    }

    private Optional<Entry> findEntry(Long id) {
        return jpaRepository.findById(id);
    }
}
