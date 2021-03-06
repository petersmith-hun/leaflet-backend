package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.EntryRepository}.
 *
 * @author Peter Smith
 */
public interface EntryDAO extends BaseDAO<Entry, Long>, SelfStatusAwareDAO<Long> {

    Entry findByLink(String link);

    List<Entry> findByUser(User user);

    List<Entry> findByCategory(Category category);

    List<Entry> findByTags(Tag tag);

    Page<Entry> findAll(Specification<Entry> specification, Pageable pageable);

    void updateAttachments(Long id, List<UploadedFile> attachments);

    void updateTags(Long id, List<Tag> tags);
}
