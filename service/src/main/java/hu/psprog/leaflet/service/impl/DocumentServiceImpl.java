package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.repository.specification.DocumentSpecification;
import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.DocumentToDocumentVOConverter;
import hu.psprog.leaflet.service.converter.DocumentVOToDocumentConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private DocumentDAO documentDAO;

    @Autowired
    private DocumentToDocumentVOConverter documentToDocumentVOConverter;

    @Autowired
    private DocumentVOToDocumentConverter documentVOToDocumentConverter;

    @Override
    public DocumentVO getOne(Long id) throws ServiceException {

        Document document = documentDAO.findOne(id);

        if (document == null) {
            throw new EntityNotFoundException(Document.class, id);
        }

        return documentToDocumentVOConverter.convert(document);
    }

    @Override
    public List<DocumentVO> getAll() {

        return documentDAO.findAll().stream()
                .map(e -> documentToDocumentVOConverter.convert(e))
                .collect(Collectors.toList());
    }

    @Override
    public EntityPageVO<DocumentVO> getPageOfPublicDocuments(int page, int limit, OrderDirection direction, DocumentVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<Document> documentPage = documentDAO.findAll(DocumentSpecification.isEnabled, pageable);

        return PageableUtil.convertPage(documentPage, documentToDocumentVOConverter);
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
    public Long count() {

        return documentDAO.count();
    }

    @Override
    public Long createOne(DocumentVO entity) throws ServiceException {

        Document document = documentVOToDocumentConverter.convert(entity);
        Document savedDocument = documentDAO.save(document);

        if (savedDocument == null) {
            throw new EntityCreationException(Document.class);
        }

        return savedDocument.getId();
    }

    @Override
    public List<Long> createBulk(List<DocumentVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (DocumentVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    public DocumentVO updateOne(Long id, DocumentVO updatedEntity) throws ServiceException {

        Document updatedDocument = documentDAO.updateOne(id, documentVOToDocumentConverter.convert(updatedEntity));

        if (updatedDocument == null) {
            throw new EntityNotFoundException(Document.class, id);
        }

        return documentToDocumentVOConverter.convert(updatedDocument);
    }

    @Override
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
    public void deleteByEntity(DocumentVO entity) throws ServiceException {

        if (!documentDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Document.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {

        try {
            documentDAO.delete(id);
        } catch (IllegalArgumentException exc){
            throw new EntityNotFoundException(Document.class, id);
        }
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!documentDAO.exists(id)) {
            throw new EntityNotFoundException(Document.class, id);
        }

        documentDAO.disable(id);
    }
}
