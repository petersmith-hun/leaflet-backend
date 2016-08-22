package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public List<Document> findByUser(User user) {
        return ((DocumentRepository) jpaRepository).findByUser(user);
    }

    @Override
    public Document updateOne(Long id, Document updatedEntity) {

        Document currentDocument = jpaRepository.getOne(id);
        if (currentDocument != null) {
            currentDocument.setTitle(updatedEntity.getTitle());
            currentDocument.setContent(updatedEntity.getContent());
            currentDocument.setSeoTitle(updatedEntity.getSeoTitle());
            currentDocument.setSeoDescription(updatedEntity.getSeoDescription());
            currentDocument.setSeoKeywords(updatedEntity.getSeoKeywords());
            currentDocument.setLink(updatedEntity.getLink());
            currentDocument.setLastModified(new Date());
            jpaRepository.flush();
        }

        return currentDocument;
    }
}
