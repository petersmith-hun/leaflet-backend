package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * DAO implementation for {@link EntryRepository}.
 *
 * @author Peter Smith
 */
@Component
public class EntryDAOImpl extends SelfStatusAwareDAOImpl<Entry, Long> implements EntryDAO {

    @Autowired
    public EntryDAOImpl(final EntryRepository entryRepository) {
        super(entryRepository);
    }

    @Override
    public Entry findByLink(String link) {
        return ((EntryRepository) jpaRepository).findByLink(link);
    }

    @Override
    public List<Entry> findByUser(User user) {
        return ((EntryRepository) jpaRepository).findByUser(user);
    }

    @Override
    public List<Entry> findByCategory(Category category) {
        return ((EntryRepository) jpaRepository).findByCategory(category);
    }

    @Override
    public List<Entry> findByTags(Tag tag) {
        return ((EntryRepository) jpaRepository).findByTags(tag);
    }

    @Override
    public Page<Entry> findAll(Specification<Entry> specification, Pageable pageable) {
        return ((EntryRepository) jpaRepository).findAll(specification, pageable);
    }

    @Transactional
    @Override
    public void updateAttachments(Long id, List<UploadedFile> attachments) {

        Entry currentEntry = jpaRepository.getOne(id);
        if (Objects.nonNull(currentEntry)) {
            currentEntry.setAttachments(attachments);
            jpaRepository.flush();
        }
    }

    @Transactional
    @Override
    public void updateTags(Long id, List<Tag> tags) {

        Entry currentEntry = jpaRepository.getOne(id);
        if (Objects.nonNull(currentEntry)) {
            currentEntry.setTags(tags);
            jpaRepository.flush();
        }
    }

    @Transactional
    @Override
    public Entry updateOne(Long id, Entry updatedEntity) {

        Entry currentEntry = jpaRepository.getOne(id);
        if (currentEntry != null) {
            currentEntry.setTitle(updatedEntity.getTitle());
            currentEntry.setContent(updatedEntity.getContent());
            currentEntry.setRawContent(updatedEntity.getRawContent());
            currentEntry.setPrologue(updatedEntity.getPrologue());
            currentEntry.setSeoTitle(updatedEntity.getSeoTitle());
            currentEntry.setSeoDescription(updatedEntity.getSeoDescription());
            currentEntry.setSeoKeywords(updatedEntity.getSeoKeywords());
            currentEntry.setLink(updatedEntity.getLink());
            currentEntry.setCategory(updatedEntity.getCategory());
            currentEntry.setLastModified(new Date());
            currentEntry.setStatus(updatedEntity.getStatus());
            currentEntry.setEnabled(updatedEntity.isEnabled());
            currentEntry.setLocale(updatedEntity.getLocale());
            jpaRepository.flush();
        }

        return currentEntry;
    }
}
