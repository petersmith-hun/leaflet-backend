package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.persistence.entity.Entry;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.AttachmentRepository}.
 *
 * @author Peter Smith
 */
public interface AttachmentDAO extends BaseDAO<Attachment, Long>, SelfStatusAwareDAO<Long> {

    public List<Attachment> findByEntry(Entry entry);

    public List<Attachment> findByEntry(Specification<Attachment> specification, Entry entry);
}
