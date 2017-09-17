package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CategoryVOToCategoryConverter;
import hu.psprog.leaflet.service.converter.EntryToEntryVOConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitAdmin;
import hu.psprog.leaflet.service.security.annotation.PermitEditorOrAdmin;
import hu.psprog.leaflet.service.security.annotation.PermitSelf;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EntryService}.
 *
 * @author Peter Smith
 */
@Service
public class EntryServiceImpl implements EntryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryServiceImpl.class);

    private static final String AN_ENTRY_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS = "An entry with the specified link already exists.";
    private static final String ENTITY_COULD_NOT_BE_PERSISTED = "Entity could not be persisted.";

    private EntryDAO entryDAO;
    private EntryToEntryVOConverter entryToEntryVOConverter;
    private EntryVOToEntryConverter entryVOToEntryConverter;
    private CategoryVOToCategoryConverter categoryVOToCategoryConverter;

    @Autowired
    public EntryServiceImpl(EntryDAO entryDAO, EntryToEntryVOConverter entryToEntryVOConverter,
                            EntryVOToEntryConverter entryVOToEntryConverter, CategoryVOToCategoryConverter categoryVOToCategoryConverter) {
        this.entryDAO = entryDAO;
        this.entryToEntryVOConverter = entryToEntryVOConverter;
        this.entryVOToEntryConverter = entryVOToEntryConverter;
        this.categoryVOToCategoryConverter = categoryVOToCategoryConverter;
    }

    @Override
    @PermitSelf.Entry
    public void deleteByID(Long id) throws ServiceException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.delete(id);
    }

    @Override
    @PermitAdmin
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    @PermitEditorOrAdmin
    public EntryVO getOne(Long id) throws ServiceException {

        Entry entry = entryDAO.findOne(id);

        if (entry == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        return entryToEntryVOConverter.convert(entry);
    }

    @Override
    @PermitEditorOrAdmin
    public List<EntryVO> getAll() {

        return entryDAO.findAll().stream()
                .map(entryToEntryVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitEditorOrAdmin
    public Long count() {

        return entryDAO.count();
    }

    @Override
    @PermitEditorOrAdmin
    public Long createOne(EntryVO entity) throws ServiceException {

        Entry entry = entryVOToEntryConverter.convert(entity);
        Entry savedEntry;
        try {
            savedEntry = entryDAO.save(entry);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(AN_ENTRY_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (savedEntry == null) {
            throw new EntityCreationException(Entry.class);
        }

        return entry.getId();
    }

    @Override
    @PermitEditorOrAdmin
    public List<Long> createBulk(List<EntryVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (EntryVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    @PermitSelf.Entry
    public EntryVO updateOne(Long id, EntryVO updatedEntity) throws ServiceException {

        Entry updatedEntry;
        try {
            updatedEntry = entryDAO.updateOne(id, entryVOToEntryConverter.convert(updatedEntity));
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(AN_ENTRY_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (updatedEntry == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        return entryToEntryVOConverter.convert(updatedEntry);
    }

    @Override
    @PermitEditorOrAdmin
    public List<EntryVO> updateBulk(Map<Long, EntryVO> updatedEntities) throws ServiceException {

        List<EntryVO> entryVOs = new LinkedList<>();

        Iterator<Map.Entry<Long, EntryVO>> entities = updatedEntities.entrySet().iterator();
        while (entities.hasNext()) {
            Map.Entry<Long, EntryVO> currentEntity = entities.next();
            EntryVO updatedEntity = updateOne(currentEntity.getKey(), currentEntity.getValue());
            entryVOs.add(updatedEntity);
        }

        return entryVOs;
    }

    @Override
    public EntryVO findByLink(String link) throws EntityNotFoundException {

        Entry entry = entryDAO.findByLink(link);

        if (entry == null) {
            throw new EntityNotFoundException(Entry.class, link);
        }

        return entryToEntryVOConverter.convert(entry);
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntries(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Specifications<Entry> specs = Specifications
                .where(EntrySpecification.IS_PUBLIC)
                .and(EntrySpecification.IS_ENABLED);
        Page<Entry> entityPage = entryDAO.findAll(specs, pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter);
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(CategoryVO categoryVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Specifications<Entry> specs = Specifications
                .where(EntrySpecification.isUnderCategory(categoryVOToCategoryConverter.convert(categoryVO)))
                .and(EntrySpecification.IS_PUBLIC)
                .and(EntrySpecification.IS_ENABLED);
        Page<Entry> entityPage = entryDAO.findAll(specs, pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter);
    }

    @Override
    @PermitSelf.Entry
    public void enable(Long id) throws EntityNotFoundException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.enable(id);
    }

    @Override
    @PermitSelf.Entry
    public void disable(Long id) throws EntityNotFoundException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.disable(id);
    }

    @Override
    @PermitEditorOrAdmin
    public EntityPageVO<EntryVO> getEntityPage(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<Entry> entityPage = entryDAO.findAll(pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter);
    }
}
