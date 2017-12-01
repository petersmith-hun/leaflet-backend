package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Document;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.DocumentRepository}.
 *
 * @author Peter Smith
 */
public interface DocumentDAO extends BaseDAO<Document, Long>, SelfStatusAwareDAO<Long> {

    List<Document> findAll(Specification<Document> specification);

    Document findByLink(String link);
}
