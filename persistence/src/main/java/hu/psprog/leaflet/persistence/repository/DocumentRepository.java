package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Document repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    /**
     * Returns {@link Document} identified by given link.
     *
     * @param link link of document
     * @return document identified by given link
     */
    public Document findByLink(String link);
}
