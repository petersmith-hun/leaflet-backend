package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Document;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.DocumentRepository}.
 *
 * @author Peter Smith
 */
public interface DocumentDAO extends BaseDAO<Document, Long>, SelfStatusAwareDAO<Long> {

    List<Document> findAll(Specification<Document> specification);

    Optional<Document> findByLink(String link);
}
