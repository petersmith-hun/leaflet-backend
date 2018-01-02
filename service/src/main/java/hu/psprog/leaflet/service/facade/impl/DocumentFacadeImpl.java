package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.DocumentFacade;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link DocumentFacade}.
 *
 * @author Peter Smith
 */
@Service
public class DocumentFacadeImpl implements DocumentFacade {

    private DocumentService documentService;

    @Autowired
    public DocumentFacadeImpl(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public DocumentVO createOne(DocumentVO entity) throws ServiceException {
        Long documentID = documentService.createOne(entity);
        return documentService.getOne(documentID);
    }

    @Override
    public DocumentVO getOne(Long id) throws ServiceException {
        return documentService.getOne(id);
    }

    @Override
    public List<DocumentVO> getAll() {
        return documentService.getAll();
    }

    @Override
    public List<DocumentVO> getPublicDocuments() {
        return documentService.getPublicDocuments();
    }

    @Override
    public DocumentVO getByLink(String link) throws ServiceException {
        return documentService.getByLink(link);
    }

    @Override
    public Long count() {
        return documentService.count();
    }

    @Override
    public DocumentVO updateOne(Long id, DocumentVO updatedDocument) throws ServiceException {
        documentService.updateOne(id, updatedDocument);
        return documentService.getOne(id);
    }

    @Override
    public void deletePermanently(Long id) throws ServiceException {
        documentService.deleteByID(id);
    }

    @Override
    public DocumentVO changeStatus(Long id) throws ServiceException {

        DocumentVO document = documentService.getOne(id);
        if (document.isEnabled()) {
            documentService.disable(id);
        } else {
            documentService.enable(id);
        }

        return documentService.getOne(id);
    }
}
