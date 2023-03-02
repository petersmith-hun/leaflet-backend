package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.EntryRepository}.
 *
 * @author Peter Smith
 */
public interface EntryDAO extends BaseDAO<Entry, Long>, SelfStatusAwareDAO<Long> {

    Optional<Entry> findByLink(String link);

    Page<Entry> findAll(Specification<Entry> specification, Pageable pageable);

    void updateAttachments(Long id, List<UploadedFile> attachments);

    void updateTags(Long id, List<Tag> tags);
}
