package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.EntryToEntryVOConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.InvalidTransitionException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.search.SearchHandler;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.util.PublishHandler;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
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
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
    private static final Map<EntryStatus, EntryStatus> VALID_TRANSITIONS = Map.of(
            EntryStatus.DRAFT, EntryStatus.REVIEW,
            EntryStatus.REVIEW, EntryStatus.PUBLIC,
            EntryStatus.PUBLIC, EntryStatus.DRAFT
    );

    private final EntryDAO entryDAO;
    private final EntryToEntryVOConverter entryToEntryVOConverter;
    private final EntryVOToEntryConverter entryVOToEntryConverter;
    private final PublishHandler publishHandler;
    private final SearchHandler<EntrySearchParametersVO, Entry> searchHandler;

    @Autowired
    public EntryServiceImpl(EntryDAO entryDAO, EntryToEntryVOConverter entryToEntryVOConverter,
                            EntryVOToEntryConverter entryVOToEntryConverter, PublishHandler publishHandler,
                            SearchHandler<EntrySearchParametersVO, Entry> searchHandler) {
        this.entryDAO = entryDAO;
        this.entryToEntryVOConverter = entryToEntryVOConverter;
        this.entryVOToEntryConverter = entryVOToEntryConverter;
        this.publishHandler = publishHandler;
        this.searchHandler = searchHandler;
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

        return entryDAO.findById(id)
                .map(entryToEntryVOConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException(Entry.class, id));
    }

    @Override
    @PermitScope.Read.Entries
    public List<EntryVO> getAll() {

        return entryDAO.findAll().stream()
                .map(entryToEntryVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitScope.Write.Entries
    public Long createOne(EntryVO entity) throws ServiceException {

        Entry entry = entryVOToEntryConverter.convert(entity);
        publishHandler.updatePublishDate(entry);

        try {
            Entry savedEntry = entryDAO.save(entry);

            LOGGER.info("New entry [{}] has been created with ID [{}]", savedEntry.getTitle(), savedEntry.getId());

            return savedEntry.getId();

        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(AN_ENTRY_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }
    }

    @Override
    @PermitScope.Write.OwnEntryOrElevated
    public EntryVO updateOne(Long id, EntryVO updatedEntity) throws ServiceException {

        try {
            Entry updatedEntryData = entryVOToEntryConverter.convert(updatedEntity);
            publishHandler.updatePublishDate(id, updatedEntryData);
            return entryDAO.updateOne(id, updatedEntryData)
                    .map(logUpdate())
                    .map(entryToEntryVOConverter::convert)
                    .orElseThrow(() -> new EntityNotFoundException(Entry.class, id));

        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(AN_ENTRY_WITH_THE_SPECIFIED_LINK_ALREADY_EXISTS, e);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }
    }

    @Override
    public EntryVO findByLink(String link) throws EntityNotFoundException {

        return entryDAO.findByLink(link)
                .map(entryToEntryVOConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException(Entry.class, link));
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntries(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        var entrySearchParametersVO = publicOnlySearchBuilder(page, limit, direction, orderBy)
                .build();

        return getPageWithWhereSpecification(entrySearchParametersVO);
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(CategoryVO categoryVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        var entrySearchParametersVO = publicOnlySearchBuilder(page, limit, direction, orderBy)
                .categoryID(Optional.ofNullable(categoryVO.getId()))
                .build();

        return getPageWithWhereSpecification(entrySearchParametersVO);
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderTag(TagVO tagVO, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        var entrySearchParametersVO = publicOnlySearchBuilder(page, limit, direction, orderBy)
                .tagID(Optional.ofNullable(tagVO.getId()))
                .build();

        return getPageWithWhereSpecification(entrySearchParametersVO);
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesByContent(String content, int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        var entrySearchParametersVO = publicOnlySearchBuilder(page, limit, direction, orderBy)
                .content(Optional.ofNullable(content))
                .build();

        return getPageWithWhereSpecification(entrySearchParametersVO);
    }

    @Override
    @PermitScope.Read.Entries
    public EntityPageVO<EntryVO> searchEntries(EntrySearchParametersVO entrySearchParametersVO) {
        return getPageWithWhereSpecification(entrySearchParametersVO);
    }

    @Override
    public List<EntryVO> getListOfPublicEntries() {

        var entrySearchParametersVO = publicOnlySearchBuilder().build();
        var publicOnlyEntriesSpecification = searchHandler.createSpecification(entrySearchParametersVO);

        return entryDAO.findAll(publicOnlyEntriesSpecification, Pageable.unpaged())
                .getContent().stream()
                .map(entryToEntryVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitScope.Write.Entries
    public EntryVO changePublicationStatus(Long id, String newStatus) throws ServiceException {

        Entry entry = entryDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Entry.class, id));

        EntryStatus newPublicationStatus = EntryStatus.valueOf(newStatus);
        assertValidTransition(entry, newPublicationStatus);
        entry.setStatus(newPublicationStatus);

        publishHandler.updatePublishDate(id, entry);
        entryDAO.updateOne(id, entry);

        LOGGER.info("Publication status of entry by ID [{}] has been updated to [{}]", id, newStatus);

        return entryToEntryVOConverter.convert(entry);
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

    private EntityPageVO<EntryVO> getPageWithWhereSpecification(EntrySearchParametersVO entrySearchParametersVO) {

        Pageable pageable = PageableUtil.createPage(
                entrySearchParametersVO.getPage(),
                entrySearchParametersVO.getLimit(),
                entrySearchParametersVO.getOrderDirection(),
                entrySearchParametersVO.getOrderBy().getField());
        Specification<Entry> specs = searchHandler.createSpecification(entrySearchParametersVO);
        Page<Entry> entityPage = entryDAO.findAll(specs, pageable);

        return PageableUtil.convertPage(entityPage, entryToEntryVOConverter);
    }

    private EntrySearchParametersVO.EntrySearchParametersVOBuilder publicOnlySearchBuilder() {

        return EntrySearchParametersVO.builder()
                .enabled(Optional.of(true))
                .status(Optional.of(EntryStatus.PUBLIC));
    }

    private EntrySearchParametersVO.EntrySearchParametersVOBuilder publicOnlySearchBuilder(int page, int limit, OrderDirection direction, EntryVO.OrderBy orderBy) {

        return publicOnlySearchBuilder()
                .page(page)
                .limit(limit)
                .orderDirection(direction)
                .orderBy(orderBy);
    }

    private Function<Entry, Entry> logUpdate() {

        return entry -> {
            LOGGER.info("Existing entry [{}] with ID [{}] has been updated", entry.getTitle(), entry.getId());
            return entry;
        };
    }

    private void assertValidTransition(Entry entry, EntryStatus newStatus) throws InvalidTransitionException {

        boolean validTransition = VALID_TRANSITIONS.entrySet()
                .stream()
                .anyMatch(transition -> transition.getKey() == entry.getStatus() && transition.getValue() == newStatus);

        if (!validTransition) {
            var invalidTransitionException = new InvalidTransitionException(entry.getId(), entry.getStatus(), newStatus);
            LOGGER.error(invalidTransitionException.getMessage());

            throw invalidTransitionException;
        }
    }
}
