package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.DocumentRepository}.
 *
 * @author Peter Smith
 */
public interface DocumentDAO extends BaseDAO<Document, Long>, SelfStatusAwareDAO<Long> {

    public Page<Document> findAll(Specification<Document> specification, Pageable pageable);

    public Document findByLink(String link);
}
