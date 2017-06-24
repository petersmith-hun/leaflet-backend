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
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
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
    public void deleteByEntity(EntryVO entity) throws ServiceException {

        if (!entryDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(Entry.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long id) throws ServiceException {

        try {
            entryDAO.delete(id);
        } catch (IllegalArgumentException exc) {
            LOGGER.error("Error occurred during deletion", exc);
            throw new EntityNotFoundException(Entry.class, id);
        }
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    public EntryVO getOne(Long id) throws ServiceException {

        Entry entry = entryDAO.findOne(id);

        if (entry == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        return entryToEntryVOConverter.convert(entry);
    }

    @Override
    public List<EntryVO> getAll() {

        return entryDAO.findAll().stream()
                .map(entry -> entryToEntryVOConverter.convert(entry))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {

        return entryDAO.count();
    }

    @Override
    public Long createOne(EntryVO entity) throws ServiceException {

        Entry entry = entryVOToEntryConverter.convert(entity);
        Entry savedEntry;
        try {
            savedEntry = entryDAO.save(entry);
        } catch (PersistenceException e) {
            throw new ConstraintViolationException(e);
        }

        if (savedEntry == null) {
            throw new EntityCreationException(Entry.class);
        }

        return entry.getId();
    }

    @Override
    public List<Long> createBulk(List<EntryVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for (EntryVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    public EntryVO updateOne(Long id, EntryVO updatedEntity) throws ServiceException {

        Entry updatedEntry;
        try {
            updatedEntry = entryDAO.updateOne(id, entryVOToEntryConverter.convert(updatedEntity));
        } catch (PersistenceException e) {
            throw new ConstraintViolationException(e);
        }

        if (updatedEntry == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        return entryToEntryVOConverter.convert(updatedEntry);
    }

    @Override
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

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter, EntryVO.class);
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(CategoryVO categoryVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Specifications<Entry> specs = Specifications
                .where(EntrySpecification.isUnderCategory(categoryVOToCategoryConverter.convert(categoryVO)))
                .and(EntrySpecification.IS_PUBLIC)
                .and(EntrySpecification.IS_ENABLED);
        Page<Entry> entityPage = entryDAO.findAll(specs, pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter, EntryVO.class);
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.disable(id);
    }

    @Override
    public EntityPageVO<EntryVO> getEntityPage(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<Entry> entityPage = entryDAO.findAll(pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter, EntryVO.class);
    }
}
