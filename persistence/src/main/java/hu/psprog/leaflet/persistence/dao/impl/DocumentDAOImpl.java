package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * DAO implementation for {@link DocumentRepository}.
 *
 * @author Peter Smith
 */
@Component
public class DocumentDAOImpl extends SelfStatusAwareDAOImpl<Document, Long> implements DocumentDAO {

    @Autowired
    public DocumentDAOImpl(final DocumentRepository documentRepository) {
        super(documentRepository);
    }

    @Override
    public List<Document> findAll(Specification<Document> specification) {
        return ((DocumentRepository) jpaRepository).findAll(specification);
    }

    @Override
    public Document findByLink(String link) {
        return ((DocumentRepository) jpaRepository).findByLink(link);
    }

    @Transactional
    @Override
    public Document updateOne(Long id, Document updatedEntity) {

        Document currentDocument = jpaRepository.getOne(id);
        if (currentDocument != null) {
            currentDocument.setTitle(updatedEntity.getTitle());
            currentDocument.setRawContent(updatedEntity.getRawContent());
            currentDocument.setSeoTitle(updatedEntity.getSeoTitle());
            currentDocument.setSeoDescription(updatedEntity.getSeoDescription());
            currentDocument.setSeoKeywords(updatedEntity.getSeoKeywords());
            currentDocument.setLink(updatedEntity.getLink());
            currentDocument.setLocale(updatedEntity.getLocale());
            currentDocument.setEnabled(updatedEntity.isEnabled());
            currentDocument.setLastModified(new Date());
            jpaRepository.flush();
        }

        return currentDocument;
    }
}
