package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.repository.specification.DocumentSpecification;
import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.converter.DocumentToDocumentVOConverter;
import hu.psprog.leaflet.service.converter.DocumentVOToDocumentConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitEditorOrAdmin;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DocumentService}.
 *
 * @author Peter Smith
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private static final String A_DOCUMENT_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS = "A document with the specified link already exists.";
    private static final String ENTITY_COULD_NOT_BE_PERSISTED = "Entity could not be persisted.";

    private DocumentDAO documentDAO;
    private DocumentToDocumentVOConverter documentToDocumentVOConverter;
    private DocumentVOToDocumentConverter documentVOToDocumentConverter;

    @Autowired
    public DocumentServiceImpl(DocumentDAO documentDAO, DocumentToDocumentVOConverter documentToDocumentVOConverter,
                               DocumentVOToDocumentConverter documentVOToDocumentConverter) {
        this.documentDAO = documentDAO;
        this.documentToDocumentVOConverter = documentToDocumentVOConverter;
        this.documentVOToDocumentConverter = documentVOToDocumentConverter;
    }

    @Override
    @PermitEditorOrAdmin
    public DocumentVO getOne(Long id) throws ServiceException {

        Document document = documentDAO.findOne(id);

        if (document == null) {
            throw new EntityNotFoundException(Document.class, id);
        }

        return documentToDocumentVOConverter.convert(document);
    }

    @Override
    @PermitEditorOrAdmin
    public List<DocumentVO> getAll() {

        return documentDAO.findAll().stream()
                .map(documentToDocumentVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentVO> getPublicDocuments() {

        return documentDAO.findAll(DocumentSpecification.IS_ENABLED).stream()
                .map(documentToDocumentVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentVO getByLink(String link) throws ServiceException {

        Document document = documentDAO.findByLink(link);

        if (document == null) {
            throw new EntityNotFoundException(Document.class, link);
        }

        return documentToDocumentVOConverter.convert(document);
    }

    @Override
    @PermitEditorOrAdmin
    public Long count() {

        return documentDAO.count();
    }

    @Override
    @PermitEditorOrAdmin
    public Long createOne(DocumentVO entity) throws ServiceException {

        Document document = documentVOToDocumentConverter.convert(entity);
        Document savedDocument;
        try {
            savedDocument = documentDAO.save(document);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(A_DOCUMENT_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (savedDocument == null) {
            throw new EntityCreationException(Document.class);
        }

        return savedDocument.getId();
    }

    @Override
    @PermitEditorOrAdmin
    public List<Long> createBulk(List<DocumentVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (DocumentVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    @PermitEditorOrAdmin
    public DocumentVO updateOne(Long id, DocumentVO updatedEntity) throws ServiceException {

        Document updatedDocument;
        try {
            updatedDocument = documentDAO.updateOne(id, documentVOToDocumentConverter.convert(updatedEntity));
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(A_DOCUMENT_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (updatedDocument == null) {
            throw new EntityNotFoundException(Document.class, id);
        }

        return documentToDocumentVOConverter.convert(updatedDocument);
    }

    @Override
    @PermitEditorOrAdmin
    public List<DocumentVO> updateBulk(Map<Long, DocumentVO> updatedEntities) throws ServiceException {

        List<DocumentVO> documentVOs = new LinkedList<>();

        Iterator<Map.Entry<Long, DocumentVO>> entities = updatedEntities.entrySet().iterator();
        while (entities.hasNext()) {
            Map.Entry<Long, DocumentVO> currentEntity = entities.next();
            DocumentVO updatedEntity = updateOne(currentEntity.getKey(), currentEntity.getValue());
            documentVOs.add(updatedEntity);
        }

        return documentVOs;
    }

    @Override
    @PermitEditorOrAdmin
    public void deleteByID(Long id) throws ServiceException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.delete(id);
    }

    @Override
    @PermitEditorOrAdmin
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    @PermitEditorOrAdmin
    public void enable(Long id) throws EntityNotFoundException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.enable(id);
    }

    @Override
    @PermitEditorOrAdmin
    public void disable(Long id) throws EntityNotFoundException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.disable(id);
    }
}
