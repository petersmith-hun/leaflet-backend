package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DocumentVO;

import java.util.List;

/**
 * Facade for {@link DocumentService}.
 * 
 * @author Peter Smith
 */
public interface DocumentFacade {

    /**
     * Passes document for persistence layer and returns ID of newly created document.
     *
     * @param entity {@link DocumentVO} value object
     * @return created document data
     */
    DocumentVO createOne(DocumentVO entity) throws ServiceException;

    /**
     * Retrieves entity of type {@link DocumentVO} specified by ID.
     *
     * @param id ID of the document
     * @return document identified by given identifier
     */
    DocumentVO getOne(Long id) throws ServiceException;

    /**
     * Retrieves all entity of type {@link DocumentVO}.
     *
     * @return list of all entities of type {@link DocumentVO}
     */
    List<DocumentVO> getAll();

    /**
     * Returns a page of public documents.
     *
     * @return list of public documents
     */
    List<DocumentVO> getPublicDocuments();

    /**
     * Returns {@link DocumentVO} identified by given link
     *
     * @param link link of document
     * @return document value object identified by given link
     * @throws ServiceException if document not found
     */
    DocumentVO getByLink(String link) throws ServiceException;

    /**
     * Returns number of documents.
     *
     * @return number of documents
     */
    Long count();

    /**
     * Updates document specified by given ID. Returns updated document.
     *
     * @param id ID of document
     * @param updatedDocument updated {@link DocumentVO}
     * @return updated document
     */
    DocumentVO updateOne(Long id, DocumentVO updatedDocument) throws ServiceException;

    /**
     * Deletes document by its identifier.
     *
     * @param id ID of the document to delete
     */
    void deletePermanently(Long id) throws ServiceException;

    /**
     * Changes document status.
     * If document is currently enabled, status will be updated to disabled and backwards.
     *
     * @param id ID of document to change status of
     * @return updated document data
     * @throws ServiceException if document cannot be found or status change cannot be performed
     */
    DocumentVO changeStatus(Long id) throws ServiceException;
}
