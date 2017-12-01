package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DocumentVO;

import java.util.List;

/**
 * Document service operations interface.
 *
 * @author Peter Smith
 */
public interface DocumentService extends CreateOperationCapableService<DocumentVO, Long>,
        ReadOperationCapableService<DocumentVO, Long>,
        UpdateOperationCapableService<DocumentVO, DocumentVO, Long>,
        DeleteOperationCapableService<DocumentVO, Long>,
        StatusChangeCapableService<Long> {

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
}
