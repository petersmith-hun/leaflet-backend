package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.EntryFacade;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link EntryFacade}.
 *
 * @author Peter Smith
 */
@Service
public class EntryFacadeImpl implements EntryFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryFacadeImpl.class);

    private EntryService entryService;

    @Autowired
    public EntryFacadeImpl(EntryService entryService) {
        this.entryService = entryService;
    }

    @Override
    public EntryVO findByLink(String link) throws EntityNotFoundException {
        return entryService.findByLink(link);
    }

    @Override
    public EntityPageVO<EntryVO> getEntityPage(int page, int limit, String direction, String orderBy) {
        return entryService.getEntityPage(page, limit, parseDirection(direction), parseOrderBy(orderBy));
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntries(int page, int limit, String direction, String orderBy) {
        return entryService.getPageOfPublicEntries(page, limit, parseDirection(direction), parseOrderBy(orderBy));
    }

    @Override
    public EntityPageVO<EntryVO> getPageOfPublicEntriesUnderCategory(Long categoryID, int page, int limit, String direction, String orderBy) {
        return entryService.getPageOfPublicEntriesUnderCategory(CategoryVO.wrapMinimumVO(categoryID), page, limit, parseDirection(direction), parseOrderBy(orderBy));
    }

    @Override
    public EntryVO createOne(EntryVO entity) throws ServiceException {
        Long entryID = entryService.createOne(entity);
        return entryService.getOne(entryID);
    }

    @Override
    public EntryVO getOne(Long id) throws ServiceException {
        return entryService.getOne(id);
    }

    @Override
    public List<EntryVO> getAll() {
        return entryService.getAll();
    }

    @Override
    public Long count() {
        return entryService.count();
    }

    @Override
    public EntryVO updateOne(Long id, EntryVO updatedEntry) throws ServiceException {
        entryService.updateOne(id, updatedEntry);
        return entryService.getOne(id);
    }

    @Override
    public void deletePermanently(Long id) throws ServiceException {
        entryService.deleteByID(id);
    }

    @Override
    public EntryVO changeStatus(Long id) throws ServiceException {

        EntryVO entryVO = entryService.getOne(id);
        if (entryVO.isEnabled()) {
            entryService.disable(id);
        } else {
            entryService.enable(id);
        }

        return entryService.getOne(id);
    }

    private EntryVO.OrderBy parseOrderBy(String by) {

        EntryVO.OrderBy orderBy = EntryVO.OrderBy.CREATED;
        try {
            orderBy = EntryVO.OrderBy.valueOf(by.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unknown order by constant [{}] specified, falling back to OrderBy.CREATED value.", by, e);
        }

        return orderBy;
    }

    private OrderDirection parseDirection(String direction) {

        OrderDirection orderDirection = OrderDirection.ASC;
        try {
            orderDirection = OrderDirection.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unknown order direction constant [{}] specified, falling back to OrderDirection.ASC value.", direction, e);
        }

        return orderDirection;
    }
}
