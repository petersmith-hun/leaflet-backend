package hu.psprog.leaflet.web.rest.controller;

import com.codahale.metrics.annotation.Timed;
import hu.psprog.leaflet.api.rest.request.tag.TagAssignmentRequestModel;
import hu.psprog.leaflet.api.rest.request.tag.TagCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.TagFacade;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.web.annotation.FillResponse;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.metrics.ExceptionHandlerCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * REST controller for tag related endpoints.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_TAGS)
public class TagsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    private static final String PATH_ASSIGN_TAG = "/assign";
    private static final String PATH_PUBLIC_TAGS = "/public";

    private static final String TAG_COULD_NOT_BE_CREATED = "Tag could not be created.";
    private static final String TAG_COULD_NOT_BE_UPDATED_WITH_ID = "Tag [{}] could not be updated";
    private static final String TAG_COULD_NOT_BE_UPDATED = "Tag could not be updated";
    private static final String TAG_COULD_NOT_BE_DELETED = "Tag [{}] could not be deleted";
    private static final String TAG_COULD_NOT_BE_FOUND = "Tag could not be found";
    private static final String STATUS_OF_TAG_COULD_NOT_BE_CHANGED = "Status of tag [{}] could not be changed";
    private static final String FAILED_TO_ASSIGN_TAG_TO_ENTRY = "Failed to assign tag to entry";
    private static final String FAILED_TO_UN_ASSIGN_TAG_FROM_ENTRY = "Failed to un-assign tag from entry";
    private static final String CANNOT_ASSIGN_NON_EXISTING_ENTITIES_TO_EACH_OTHER = "Cannot assign non-existing entities to each other.";
    private static final String CANNOT_UN_ASSIGN_NON_EXISTING_ENTITIES_TO_EACH_OTHER = "Cannot un-assign non-existing entities to each other.";

    private final TagFacade tagFacade;

    @Autowired
    public TagsController(ConversionService conversionService, ExceptionHandlerCounters exceptionHandlerCounters,
                          TagFacade tagFacade) {
        super(conversionService, exceptionHandlerCounters);
        this.tagFacade = tagFacade;
    }

    /**
     * GET /tags
     * Retrieves all existing tags.
     *
     * @return list of tags
     */
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public ResponseEntity<TagListDataModel> getAllTags() {

        List<TagVO> tags = tagFacade.getAll();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(tags, TagListDataModel.class));
    }

    /**
     * GET /tags/public
     * Retrieves existing public tags.
     *
     * @return list of public tags
     */
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, value = PATH_PUBLIC_TAGS)
    @Timed
    public ResponseEntity<TagListDataModel> getAllPublicTags() {

        List<TagVO> tags = tagFacade.getPublicTags();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(tags, TagListDataModel.class));
    }

    /**
     * GET /tags/{id}
     * Retrieves an existing tag.
     *
     * @param tagID ID of the tag to retrieve
     * @return tag data
     * @throws ResourceNotFoundException if the requested tag could not be found
     */
    @RequestMapping(method = RequestMethod.GET, value = PATH_PART_ID)
    public ResponseEntity<TagDataModel> getTag(@PathVariable(PATH_VARIABLE_ID) Long tagID) throws ResourceNotFoundException {

        try {
            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(tagFacade.getOne(tagID), TagDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(TAG_COULD_NOT_BE_FOUND, e);
            throw new ResourceNotFoundException(TAG_COULD_NOT_BE_FOUND, e);
        }
    }

    /**
     * POST /tags
     * Creates a new tag.
     *
     * @param tagCreateRequestModel tag data as {@link TagCreateRequestModel}.
     * @param bindingResult validation result
     * @return created tag's data
     * @throws RequestCouldNotBeFulfilledException if the tag could not be created
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseBodyDataModel> createTag(@RequestBody @Valid TagCreateRequestModel tagCreateRequestModel,
                                                       BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                TagVO tagVO = tagFacade.createOne(conversionService.convert(tagCreateRequestModel, TagVO.class));

                return ResponseEntity
                        .created(buildLocation(tagVO.getId()))
                        .body(conversionService.convert(tagVO, TagDataModel.class));
            } catch (ServiceException e) {
                LOGGER.error(TAG_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(TAG_COULD_NOT_BE_CREATED, e);
            }
        }
    }

    /**
     * PUT /tags/{id}
     * Updates an existing tag.
     *
     * @param id ID of an existing tag
     * @param tagCreateRequestModel tag data as {@link TagCreateRequestModel}.
     * @param bindingResult validation result
     * @return updated tag's data
     * @throws RequestCouldNotBeFulfilledException if the tag could not be created
     */
    @RequestMapping(method = RequestMethod.PUT, value = PATH_PART_ID)
    public ResponseEntity<BaseBodyDataModel> updateTag(@PathVariable(PATH_VARIABLE_ID) Long id,
                                  @RequestBody @Valid TagCreateRequestModel tagCreateRequestModel,
                                  BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                TagVO tagVO = tagFacade.updateOne(id, conversionService.convert(tagCreateRequestModel, TagVO.class));

                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(tagVO, TagDataModel.class));
            } catch (ServiceException e) {
                LOGGER.error(TAG_COULD_NOT_BE_UPDATED_WITH_ID, id);
                throw new RequestCouldNotBeFulfilledException(TAG_COULD_NOT_BE_UPDATED, e);
            }
        }
    }

    /**
     * DELETE /tags/{id}
     * Deletes an existing tag.
     *
     * @param id ID of an existing tag
     * @throws ResourceNotFoundException if no tag found under given ID
     */
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_PART_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            tagFacade.deletePermanently(id);
        } catch (ServiceException e) {
            LOGGER.error(TAG_COULD_NOT_BE_DELETED, id);
            throw new ResourceNotFoundException(TAG_COULD_NOT_BE_FOUND, e);
        }
    }

    /**
     * PUT /tags/{id}/status
     * Changes given tag's status (enabled/disabled).
     *
     * @param id ID of an existing tag
     * @return updated tag's data
     * @throws ResourceNotFoundException if no tag found under given ID
     */
    @RequestMapping(method = RequestMethod.PUT, value = PATH_CHANGE_STATUS)
    public ResponseEntity<TagDataModel> changeStatus(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            TagVO updatedTagVO = tagFacade.changeStatus(id);

            return ResponseEntity
                    .created(buildLocation(id))
                    .body(conversionService.convert(updatedTagVO, TagDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(STATUS_OF_TAG_COULD_NOT_BE_CHANGED, id);
            throw new ResourceNotFoundException(TAG_COULD_NOT_BE_FOUND, e);
        }
    }

    /**
     * POST /tags/assign
     * Assigns a tag to an entry.
     *
     * @param tagAssignmentRequestModel IDs of the entry and the tag to assign as {@link TagAssignmentRequestModel}
     * @param bindingResult validation result
     * @return validation information on failed validation, null otherwise
     * @throws ResourceNotFoundException if no tag or entry found under given IDs
     */
    @RequestMapping(method = RequestMethod.POST, value = PATH_ASSIGN_TAG)
    public ResponseEntity<BaseBodyDataModel> attachTag(@RequestBody @Valid TagAssignmentRequestModel tagAssignmentRequestModel,
                                  BindingResult bindingResult) throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                tagFacade.attachTagToEntry(conversionService.convert(tagAssignmentRequestModel, TagAssignmentVO.class));
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(null);
            } catch (ServiceException e) {
                LOGGER.error(FAILED_TO_ASSIGN_TAG_TO_ENTRY, e);
                throw new ResourceNotFoundException(CANNOT_ASSIGN_NON_EXISTING_ENTITIES_TO_EACH_OTHER, e);
            }
        }
    }

    /**
     * PUT /tags/assign
     * Un-assigns a tag from an entry.
     *
     * @param tagAssignmentRequestModel IDs of the entry and the tag to un-assign as {@link TagAssignmentRequestModel}
     * @param bindingResult validation result
     * @return validation information on failed validation, null otherwise
     * @throws ResourceNotFoundException if no tag or entry found under given IDs
     */
    @RequestMapping(method = RequestMethod.PUT, value = PATH_ASSIGN_TAG)
    public ResponseEntity<BaseBodyDataModel> detachTag(@RequestBody @Valid TagAssignmentRequestModel tagAssignmentRequestModel,
                                  BindingResult bindingResult) throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                tagFacade.detachTagFromEntry(conversionService.convert(tagAssignmentRequestModel, TagAssignmentVO.class));
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(null);
            } catch (ServiceException e) {
                LOGGER.error(FAILED_TO_UN_ASSIGN_TAG_FROM_ENTRY, e);
                throw new ResourceNotFoundException(CANNOT_UN_ASSIGN_NON_EXISTING_ENTITIES_TO_EACH_OTHER, e);
            }
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_TAGS + "/" + id);
    }
}
