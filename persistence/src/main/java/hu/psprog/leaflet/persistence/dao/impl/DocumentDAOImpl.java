package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    public Page<Document> findAll(Specification<Document> specification, Pageable pageable) {
        return ((JpaSpecificationExecutor) jpaRepository).findAll(specification, pageable);
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
