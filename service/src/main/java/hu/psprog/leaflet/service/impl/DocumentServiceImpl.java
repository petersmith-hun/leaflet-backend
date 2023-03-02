package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.repository.specification.DocumentSpecification;
import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.converter.DocumentToDocumentVOConverter;
import hu.psprog.leaflet.service.converter.DocumentVOToDocumentConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
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

    private final DocumentDAO documentDAO;
    private final DocumentToDocumentVOConverter documentToDocumentVOConverter;
    private final DocumentVOToDocumentConverter documentVOToDocumentConverter;

    @Autowired
    public DocumentServiceImpl(DocumentDAO documentDAO, DocumentToDocumentVOConverter documentToDocumentVOConverter,
                               DocumentVOToDocumentConverter documentVOToDocumentConverter) {
        this.documentDAO = documentDAO;
        this.documentToDocumentVOConverter = documentToDocumentVOConverter;
        this.documentVOToDocumentConverter = documentVOToDocumentConverter;
    }

    @Override
    @PermitScope.Read.Documents
    public DocumentVO getOne(Long id) throws ServiceException {

        return documentDAO.findById(id)
                .map(documentToDocumentVOConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException(Document.class, id));
    }

    @Override
    @PermitScope.Read.Documents
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

        return documentDAO.findByLink(link)
                .map(documentToDocumentVOConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException(Document.class, link));
    }

    @Override
    @PermitScope.Write.Documents
    public Long createOne(DocumentVO entity) throws ServiceException {

        try {
            Document document = documentVOToDocumentConverter.convert(entity);
            Document savedDocument = documentDAO.save(document);

            LOGGER.info("New document [{}] has been created with ID [{}]", savedDocument.getTitle(), savedDocument.getId());

            return savedDocument.getId();

        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(A_DOCUMENT_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }
    }

    @Override
    @PermitScope.Write.Documents
    public DocumentVO updateOne(Long id, DocumentVO updatedEntity) throws ServiceException {

        try {
            return documentDAO.updateOne(id, documentVOToDocumentConverter.convert(updatedEntity))
                    .map(logUpdate())
                    .map(documentToDocumentVOConverter::convert)
                    .orElseThrow(() -> new EntityNotFoundException(Document.class, id));

        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(A_DOCUMENT_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }
    }

    @Override
    @PermitScope.Write.Documents
    public void deleteByID(Long id) throws ServiceException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.delete(id);
        LOGGER.info("Deleted document of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Documents
    public void enable(Long id) throws EntityNotFoundException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.enable(id);
        LOGGER.info("Enabled document of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.Documents
    public void disable(Long id) throws EntityNotFoundException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.disable(id);
        LOGGER.info("Disabled document of ID [{}]", id);
    }

    private Function<Document, Document> logUpdate() {

        return document -> {
            LOGGER.info("Existing document [{}] with ID [{}] has been updated", document.getTitle(), document.getId());
            return document;
        };
    }
}
