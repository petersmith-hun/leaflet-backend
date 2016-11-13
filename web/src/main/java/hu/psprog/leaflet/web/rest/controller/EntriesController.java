package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private static final String PATH_ENTRY_BY_LINK = "/link" + PATH_PART_LINK;
    private static final String PATH_ENTRY_BY_ID = "/id" + PATH_PART_ID;

    /**
     * GET /entries
     * Returns basic information of all existing entry.
     *
     * @return list of existing entries
     */
    @RequestMapping(method = RequestMethod.GET)
    public BaseBodyDataModel getAllEntries() {

        return null;
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
    @RequestMapping(method = RequestMethod.GET, value = PATH_PAGE_OF_ENTRIES)
    public BaseBodyDataModel getPageOfPublicEntries(@PathVariable(BaseController.PATH_VARIABLE_PAGE) int page,
                                                    @RequestParam(name = REQUEST_PARAMETER_LIMIT, defaultValue = PAGINATION_DEFAULT_LIMIT) int limit,
                                                    @RequestParam(name = REQUEST_PARAMETER_ORDER_BY, defaultValue = PAGINATION_DEFAULT_ORDER_BY) String orderBy,
                                                    @RequestParam(name = REQUEST_PARAMETER_ORDER_DIRECTION, defaultValue = PAGINATION_DEFAULT_ORDER_DIRECTION) String orderDirection) {

        return null;
    }

    /**
     * GET /entries/link/{link}
     * Returns entry identified by given link.
     *
     * @param link link to identify entry
     * @return identified entry
     */
    @RequestMapping(method = RequestMethod.GET, value = PATH_ENTRY_BY_LINK)
    public BaseBodyDataModel getEntryByLink(@PathVariable(BaseController.PATH_VARIABLE_LINK) String link) {

        return null;
    }

    /**
     * GET /entries/id/{id}
     * Returns entry identified by given ID.
     *
     * @param id id to identify entry
     * @return identified entry
     */
    @RequestMapping(method = RequestMethod.GET, value = PATH_ENTRY_BY_ID)
    public BaseBodyDataModel getEntryByID(@PathVariable(BaseController.PATH_VARIABLE_ID) Long id) {

        return null;
    }
}
