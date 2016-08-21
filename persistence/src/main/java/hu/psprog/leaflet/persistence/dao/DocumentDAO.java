package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.User;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.DocumentRepository}.
 *
 * @author Peter Smith
 */
public interface DocumentDAO extends BaseDAO<Document, Long>, SelfStatusAwareDAO<Long> {

    public List<Document> findByUser(User user);
}
