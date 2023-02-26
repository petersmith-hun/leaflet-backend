package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DAO implementation for {@link DocumentRepository}.
 *
 * @author Peter Smith
 */
@Component
public class DocumentDAOImpl extends SelfStatusAwareDAOImpl<Document, Long> implements DocumentDAO {

    @Autowired
    public DocumentDAOImpl(final DocumentRepository documentRepository, JpaContext jpaContext) {
        super(documentRepository, jpaContext);
    }

    @Override
    public List<Document> findAll(Specification<Document> specification) {
        return ((DocumentRepository) jpaRepository).findAll(specification);
    }

    @Override
    public Document findByLink(String link) {
        return ((DocumentRepository) jpaRepository).findByLink(link);
    }

    @Override
    protected void doUpdate(Document currentEntity, Document updatedEntity) {

        currentEntity.setTitle(updatedEntity.getTitle());
        currentEntity.setRawContent(updatedEntity.getRawContent());
        currentEntity.setSeoTitle(updatedEntity.getSeoTitle());
        currentEntity.setSeoDescription(updatedEntity.getSeoDescription());
        currentEntity.setSeoKeywords(updatedEntity.getSeoKeywords());
        currentEntity.setLink(updatedEntity.getLink());
        currentEntity.setLocale(updatedEntity.getLocale());
        currentEntity.setEnabled(updatedEntity.isEnabled());
    }
}
