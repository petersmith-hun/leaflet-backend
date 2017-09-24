package hu.psprog.leaflet.web.rest.controller;

import com.codahale.metrics.annotation.Timed;
import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.annotation.FillResponse;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * REST controller for blog entry related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_ENTRIES)
public class EntriesController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntriesController.class);

    private static final String PATH_PAGE_OF_ENTRIES = "/page" + PATH_PART_PAGE;
    private static final String PATH_PAGE_OF_ENTRIES_NON_FILTERED = "/page" + PATH_PART_PAGE + "/all";
    private static final String PATH_PAGE_OF_ENTRIES_BY_CATEGORY = PATH_PART_ID + "/page" + PATH_PART_PAGE;
    private static final String PATH_ENTRY_BY_LINK = "/link" + PATH_PART_LINK;

    private static final String ENTRY_COULD_NOT_BE_CREATED = "Entry could not be created. See details: ";
    private static final String BLOG_ENTRY_COULD_NOT_BE_CREATED = "Blog entry could not be created, please try again later!";
    private static final String THE_ENTRY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING = "The entry you are looking for is not existing.";
    private static final String REQUESTED_ENTRY_NOT_FOUND = "Requested entry not found";
    private static final String AN_ENTRY_WITH_THE_SAME_LINK_ALREADY_EXISTS = "An entry with the same 'link' already exists.";

    private EntryService entryService;

    @Autowired
    public EntriesController(EntryService entryService) {
        this.entryService = entryService;
    }

    /**
     * GET /entries
     * Returns basic information of all existing entry.
     *
     * @return list of existing entries
     */
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public ResponseEntity<EntryListDataModel> getAllEntries() {

        List<EntryVO> entries = entryService.getAll();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(entries, EntryListDataModel.class));
    }

    /**
     * GET /entries/page/{page}
     * Returns basic information of given page of public entries.
     *
     * @param page page number (page indexing starts at 1)
     * @param limit (optional) number of entries on one page; defaults to {@code PAGINATION_DEFAULT_LIMIT}
     * @param orderBy (optional) order by (CREATED|TITLE); defaults to {@code CREATED}
     * @param orderDirection (optional) order direction (ASC|DESC); defaults to {@code ASC}
     * @return page of public entries
     */
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, value = PATH_PAGE_OF_ENTRIES)
    @Timed
    public ResponseEntity<EntryListDataModel> getPageOfPublicEntries(
            @PathVariable(BaseController.PATH_VARIABLE_PAGE) int page,
            @RequestParam(name = REQUEST_PARAMETER_LIMIT, defaultValue = PAGINATION_DEFAULT_LIMIT) int limit,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_BY, defaultValue = PAGINATION_DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_DIRECTION, defaultValue = PAGINATION_DEFAULT_ORDER_DIRECTION) String orderDirection) {

        EntityPageVO<EntryVO> entryPage =
                entryService.getPageOfPublicEntries(page, limit, OrderDirection.valueOf(orderDirection), EntryVO.OrderBy.valueOf(orderBy));

        return ResponseEntity
                .ok()
                .body(conversionService.convert(entryPage.getEntitiesOnPage(), EntryListDataModel.class));
    }


    /**
     * GET /entries/page/{page}/all
     * Returns basic information of given page of (public and non-public) entries.
     *
     * @param page page number (page indexing starts at 1)
     * @param limit (optional) number of entries on one page; defaults to {@code PAGINATION_DEFAULT_LIMIT}
     * @param orderBy (optional) order by (CREATED|TITLE); defaults to {@code CREATED}
     * @param orderDirection (optional) order direction (ASC|DESC); defaults to {@code ASC}
     * @return page of entries
     */
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, value = PATH_PAGE_OF_ENTRIES_NON_FILTERED)
    @Timed
    public ResponseEntity<EntryListDataModel> getPageOfEntries(
            @PathVariable(BaseController.PATH_VARIABLE_PAGE) int page,
            @RequestParam(name = REQUEST_PARAMETER_LIMIT, defaultValue = PAGINATION_DEFAULT_LIMIT) int limit,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_BY, defaultValue = PAGINATION_DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_DIRECTION, defaultValue = PAGINATION_DEFAULT_ORDER_DIRECTION) String orderDirection) {

        EntityPageVO<EntryVO> entryPage =
                entryService.getEntityPage(page, limit, OrderDirection.valueOf(orderDirection), EntryVO.OrderBy.valueOf(orderBy));

        return ResponseEntity
                .ok()
                .body(conversionService.convert(entryPage.getEntitiesOnPage(), EntryListDataModel.class));
    }

    /**
     * GET /entries/{id}/page/{page}
     * Returns basic information of given page of public entries filtered by given category ID.
     *
     * @param id category ID to filter by
     * @param page page number (page indexing starts at 1)
     * @param limit (optional) number of entries on one page; defaults to {@code PAGINATION_DEFAULT_LIMIT}
     * @param orderBy (optional) order by (CREATED|TITLE); defaults to {@code CREATED}
     * @param orderDirection (optional) order direction (ASC|DESC); defaults to {@code ASC}
     * @return page of public entries
     */
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, value = PATH_PAGE_OF_ENTRIES_BY_CATEGORY)
    @Timed
    public ResponseEntity<EntryListDataModel> getPageOfPublicEntriesByCategory(
            @PathVariable(BaseController.PATH_VARIABLE_ID) Long id,
            @PathVariable(BaseController.PATH_VARIABLE_PAGE) int page,
            @RequestParam(name = REQUEST_PARAMETER_LIMIT, defaultValue = PAGINATION_DEFAULT_LIMIT) int limit,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_BY, defaultValue = PAGINATION_DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_DIRECTION, defaultValue = PAGINATION_DEFAULT_ORDER_DIRECTION) String orderDirection) {

        EntityPageVO<EntryVO> entryPage =
                entryService.getPageOfPublicEntriesUnderCategory(CategoryVO.wrapMinimumVO(id),
                        page, limit, OrderDirection.valueOf(orderDirection), EntryVO.OrderBy.valueOf(orderBy));

        return ResponseEntity
                .ok()
                .body(conversionService.convert(entryPage.getEntitiesOnPage(), EntryListDataModel.class));
    }

    /**
     * GET /entries/link/{link}
     * Returns entry identified by given link.
     *
     * @param link link to identify entry
     * @return identified entry
     */
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, value = PATH_ENTRY_BY_LINK)
    @Timed
    public ResponseEntity<ExtendedEntryDataModel> getEntryByLink(@PathVariable(BaseController.PATH_VARIABLE_LINK) String link)
            throws ResourceNotFoundException {
        try {
            EntryVO entryVO = entryService.findByLink(link);

            return ResponseEntity.ok(conversionService.convert(entryVO, ExtendedEntryDataModel.class));
        } catch (EntityNotFoundException e) {
            LOGGER.error(REQUESTED_ENTRY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_ENTRY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * GET /entries/id/{id}
     * Returns entry identified by given ID.
     *
     * @param id ID of an existing entry
     * @return identified entry
     */
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, value = PATH_PART_ID)
    @Timed
    public ResponseEntity<EditEntryDataModel> getEntryByID(@PathVariable(BaseController.PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            EntryVO entryVO = entryService.getOne(id);

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(entryVO, EditEntryDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_ENTRY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_ENTRY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * POST /entries
     * Creates a new entry.
     *
     * @param entryCreateRequestModel entry data
     * @param bindingResult validation results
     * @return created entry data
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseBodyDataModel> createEntry(@RequestBody @Valid EntryCreateRequestModel entryCreateRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                Long entryID = entryService.createOne(conversionService.convert(entryCreateRequestModel, EntryVO.class));
                EntryVO createdEntry = entryService.getOne(entryID);

                return ResponseEntity
                        .created(buildLocation(entryID))
                        .body(conversionService.convert(createdEntry, EditEntryDataModel.class));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(AN_ENTRY_WITH_THE_SAME_LINK_ALREADY_EXISTS);
            } catch (ServiceException e) {
                LOGGER.error(ENTRY_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(BLOG_ENTRY_COULD_NOT_BE_CREATED);
            }
        }
    }

    /**
     * PUT /entries/{id}
     * Updates an existing entry.
     *
     * @param id ID of an existing entry
     * @param entryUpdateRequestModel entry data
     * @param bindingResult validation results
     * @return updated entry data
     */
    @RequestMapping(method = RequestMethod.PUT, value = PATH_PART_ID)
    public ResponseEntity<BaseBodyDataModel> updateEntry(@PathVariable(PATH_VARIABLE_ID) Long id,
                                    @RequestBody @Valid EntryUpdateRequestModel entryUpdateRequestModel, BindingResult bindingResult)
            throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                entryService.updateOne(id, conversionService.convert(entryUpdateRequestModel, EntryVO.class));
                EntryVO entryVO = entryService.getOne(id);

                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(entryVO, EditEntryDataModel.class));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(AN_ENTRY_WITH_THE_SAME_LINK_ALREADY_EXISTS);
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_ENTRY_NOT_FOUND, e);
                throw new ResourceNotFoundException(THE_ENTRY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
            }
        }
    }

    /**
     * PUT /entries/{id}/status
     * Changes status of an existing entry.
     *
     * @param id ID of an existing entry
     * @return updated entry data
     */
    @RequestMapping(method = RequestMethod.PUT, value = PATH_CHANGE_STATUS)
    public ResponseEntity<EditEntryDataModel> changeStatus(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            EntryVO entryVO = entryService.getOne(id);
            if (entryVO.isEnabled()) {
                entryService.disable(id);
            } else {
                entryService.enable(id);
            }
            EntryVO updatedEntryVO = entryService.getOne(id);

            return ResponseEntity
                    .created(buildLocation(id))
                    .body(conversionService.convert(updatedEntryVO, EditEntryDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_ENTRY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_ENTRY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * DELETE /entries/{id}
     * Deletes an existing entry.
     *
     * @param id ID of an existing entry
     */
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_PART_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            entryService.deleteByID(id);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_ENTRY_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_ENTRY_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_ENTRIES + "/" + id);
    }
}
