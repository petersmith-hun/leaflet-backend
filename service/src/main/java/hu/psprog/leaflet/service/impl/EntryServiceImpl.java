package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CategoryVOToCategoryConverter;
import hu.psprog.leaflet.service.converter.EntryToEntryVOConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.converter.TagVOToTagConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.util.PublishHandler;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private static final Specification<Entry> PUBLIC_ENTRIES_SPECIFICATION = Specification
            .where(EntrySpecification.IS_PUBLIC)
            .and(EntrySpecification.IS_ENABLED);

    private final EntryDAO entryDAO;
    private final EntryToEntryVOConverter entryToEntryVOConverter;
    private final EntryVOToEntryConverter entryVOToEntryConverter;
    private final CategoryVOToCategoryConverter categoryVOToCategoryConverter;
    private final TagVOToTagConverter tagVOToTagConverter;
    private final PublishHandler publishHandler;

    @Autowired
    public EntryServiceImpl(EntryDAO entryDAO, EntryToEntryVOConverter entryToEntryVOConverter, EntryVOToEntryConverter entryVOToEntryConverter,
                            CategoryVOToCategoryConverter categoryVOToCategoryConverter, TagVOToTagConverter tagVOToTagConverter, PublishHandler publishHandler) {
        this.entryDAO = entryDAO;
        this.entryToEntryVOConverter = entryToEntryVOConverter;
        this.entryVOToEntryConverter = entryVOToEntryConverter;
        this.categoryVOToCategoryConverter = categoryVOToCategoryConverter;
        this.tagVOToTagConverter = tagVOToTagConverter;
        this.publishHandler = publishHandler;
    }

    @Override
    @PermitScope.Write.OwnEntryOrElevated
    public void deleteByID(Long id) throws ServiceException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.delete(id);
        LOGGER.info("Deleted entry of ID [{}]", id);
    }

    @Override
    @PermitScope.Read.Entries
    public EntryVO getOne(Long id) throws ServiceException {

        Entry entry = entryDAO.findOne(id);

        if (entry == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        return entryToEntryVOConverter.convert(entry);
    }

    @Override
    @PermitScope.Read.Entries
    public List<EntryVO> getAll() {

        return entryDAO.findAll().stream()
                .map(entryToEntryVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitScope.Read.Entries
    public Long count() {

        return entryDAO.count();
    }

    @Override
    @PermitScope.Write.Entries
    public Long createOne(EntryVO entity) throws ServiceException {

        Entry entry = entryVOToEntryConverter.convert(entity);
        publishHandler.updatePublishDate(entry);
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

        LOGGER.info("New entry [{}] has been created with ID [{}]", savedEntry.getTitle(), savedEntry.getId());

        return entry.getId();
    }

    @Override
    @PermitScope.Write.OwnEntryOrElevated
    public EntryVO updateOne(Long id, EntryVO updatedEntity) throws ServiceException {

        Entry updatedEntry;
        try {
            Entry updatedEntryData = entryVOToEntryConverter.convert(updatedEntity);
            publishHandler.updatePublishDate(id, updatedEntryData);
            updatedEntry = entryDAO.updateOne(id, updatedEntryData);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(AN_ENTRY_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (updatedEntry == null) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        LOGGER.info("Existing entry [{}] with ID [{}] has been updated", updatedEntry.getTitle(), id);

        return entryToEntryVOConverter.convert(updatedEntry);
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
        Page<Entry> entityPage = entryDAO.findAll(PUBLIC_ENTRIES_SPECIFICATION, pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter);
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(CategoryVO categoryVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {
        Category category = categoryVOToCategoryConverter.convert(categoryVO);
        return getPageWithWhereSpecification(page, limit, direction, orderBy, EntrySpecification.isUnderCategory(category));
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderTag(TagVO tagVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {
        Tag tag = tagVOToTagConverter.convert(tagVO);
        return getPageWithWhereSpecification(page, limit, direction, orderBy, EntrySpecification.isUnderTag(tag));
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesByContent(String content, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {
        return getPageWithWhereSpecification(page, limit, direction, orderBy, EntrySpecification.containsExpression(content));
    }

    @Override
    public List<EntryVO> getListOfPublicEntries() {

        return entryDAO.findAll(PUBLIC_ENTRIES_SPECIFICATION, Pageable.unpaged()).getContent().stream()
                .map(entryToEntryVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitScope.Write.OwnEntryOrElevated
    public void enable(Long id) throws EntityNotFoundException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.enable(id);
        LOGGER.info("Enabled entry of ID [{}]", id);
    }

    @Override
    @PermitScope.Write.OwnEntryOrElevated
    public void disable(Long id) throws EntityNotFoundException {

        if (!entryDAO.exists(id)) {
            throw new EntityNotFoundException(Entry.class, id);
        }

        entryDAO.disable(id);
        LOGGER.info("Disabled entry of ID [{}]", id);
    }

    @Override
    @PermitScope.Read.Entries
    public EntityPageVO<EntryVO> getEntityPage(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<Entry> entityPage = entryDAO.findAll(pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter);
    }

    private EntityPageVO<EntryVO> getPageWithWhereSpecification(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy, Specification<Entry> whereSpecification) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Specification<Entry> specs = Specification
                .where(whereSpecification)
                .and(EntrySpecification.IS_PUBLIC)
                .and(EntrySpecification.IS_ENABLED);
        Page<Entry> entityPage = entryDAO.findAll(specs, pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter);
    }
}
