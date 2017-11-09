package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Document;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.DocumentRepository}.
 *
 * @author Peter Smith
 */
public interface DocumentDAO extends BaseDAO<Document, Long>, SelfStatusAwareDAO<Long> {

    Document findByLink(String link);
}
