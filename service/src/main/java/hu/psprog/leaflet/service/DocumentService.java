package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;

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
     * @param page page number
     * @param limit maximum number of items on a page
     * @param direction order direction
     * @param orderBy order by field
     * @return page of public documents
     */
    public EntityPageVO<DocumentVO> getPageOfPublicDocuments(int page, int limit, OrderDirection direction, DocumentVO.OrderBy orderBy);

    /**
     * Returns {@link DocumentVO} identified by given link
     *
     * @param link link of document
     * @return document value object identified by given link
     * @throws ServiceException if document not found
     */
    public DocumentVO getByLink(String link) throws ServiceException;
}
