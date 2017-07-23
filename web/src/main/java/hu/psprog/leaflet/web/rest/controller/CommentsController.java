package hu.psprog.leaflet.web.rest.controller;

import com.codahale.metrics.annotation.Timed;
import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CommentFacade;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.annotation.AuthenticatedRequest;
import hu.psprog.leaflet.web.annotation.FillResponse;
import hu.psprog.leaflet.web.annotation.ResponseFillMode;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

/**
 * REST controller for comment related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(path = BaseController.BASE_PATH_COMMENTS)
@Validated
public class CommentsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentsController.class);

    private static final String PATH_PUBLIC_COMMENTS_FOR_ENTRY = "/entry" + PATH_PART_ID + PATH_PART_PAGE;
    private static final String PATH_ALL_COMMENTS_FOR_ENTRY = PATH_PUBLIC_COMMENTS_FOR_ENTRY + "/all";
    private static final String PATH_PERMANENT_DELETION = PATH_PART_ID + "/permanent";
    private static final String REQUESTED_COMMENT_NOT_FOUND = "Requested comment not found";
    private static final String THE_COMMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING = "The comment you are looking for is not existing";
    private static final String COMMENT_COULD_NOT_BE_CREATED = "Comment could not be created";
    private static final String YOUR_COMMENT_COULD_NOT_BE_CREATED = "Your comment could not be created, please try again later!";
    private static final String ENTRY_TO_ASSOCIATE_COMMENT_WITH_COULD_NOT_BE_FOUND = "Entry to associate comment with could not be found";

    private CommentFacade commentFacade;

    @Autowired
    public CommentsController(CommentFacade commentFacade) {
        this.commentFacade = commentFacade;
    }

    /**
     * GET /comments/entry/{id}/{page}
     * Retrieves given page of public comments for given entry.
     * Should only be used for retrieving page > 1 of comments as first page will be automatically loaded for entry.
     *
     * @param entryID ID of entry to retrieve comments for
     * @param page page number
     * @param limit (optional) number of comments on one page; defaults to {@code PAGINATION_DEFAULT_LIMIT}
     * @param orderBy (optional) order by (CREATED|TITLE); defaults to {@code CREATED}
     * @param orderDirection (optional) order direction (ASC|DESC); defaults to {@code ASC}
     * @return list of comments
     */
    @FillResponse(fill = ResponseFillMode.AJAX)
    @RequestMapping(method = RequestMethod.GET, path = PATH_PUBLIC_COMMENTS_FOR_ENTRY)
    @Timed
    public ResponseEntity<CommentListDataModel> getPageOfPublicCommentsForEntry(
            @PathVariable(PATH_VARIABLE_ID) Long entryID,
            @PathVariable(PATH_VARIABLE_PAGE) int page,
            @RequestParam(name = REQUEST_PARAMETER_LIMIT, defaultValue = PAGINATION_DEFAULT_LIMIT) int limit,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_BY, defaultValue = PAGINATION_DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_DIRECTION, defaultValue = PAGINATION_DEFAULT_ORDER_DIRECTION) String orderDirection) {

        EntityPageVO<CommentVO> comments = commentFacade.getPageOfPublicCommentsForEntry(page, limit,
                OrderDirection.valueOf(orderDirection), CommentVO.OrderBy.valueOf(orderBy), EntryVO.wrapMinimumVO(entryID));

        return ResponseEntity
                .ok()
                .body(conversionService.convert(comments.getEntitiesOnPage(), CommentListDataModel.class));
    }

    /**
     * GET /comments/entry/{id}/{page}/all
     * Retrieves given page of comments for given entry (public and non-public comments as well).
     * Should be used for admin operations.
     *
     * @param entryID ID of entry to retrieve comments for
     * @param page page number
     * @param limit (optional) number of comments on one page; defaults to {@code PAGINATION_DEFAULT_LIMIT}
     * @param orderBy (optional) order by (CREATED|TITLE); defaults to {@code CREATED}
     * @param orderDirection (optional) order direction (ASC|DESC); defaults to {@code ASC}
     * @return list of comments
     */
    @FillResponse(fill = ResponseFillMode.AJAX)
    @RequestMapping(method = RequestMethod.GET, path = PATH_ALL_COMMENTS_FOR_ENTRY)
    @Timed
    public ResponseEntity<CommentListDataModel> getPageOfCommentsForEntry(
            @PathVariable(PATH_VARIABLE_ID) Long entryID,
            @PathVariable(PATH_VARIABLE_PAGE) int page,
            @RequestParam(name = REQUEST_PARAMETER_LIMIT, defaultValue = PAGINATION_DEFAULT_LIMIT) int limit,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_BY, defaultValue = PAGINATION_DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(name = REQUEST_PARAMETER_ORDER_DIRECTION, defaultValue = PAGINATION_DEFAULT_ORDER_DIRECTION) String orderDirection) {

        EntityPageVO<CommentVO> comments = commentFacade.getPageOfCommentsForEntry(page, limit,
                OrderDirection.valueOf(orderDirection), CommentVO.OrderBy.valueOf(orderBy), EntryVO.wrapMinimumVO(entryID));

        return ResponseEntity
                .ok()
                .body(conversionService.convert(comments.getEntitiesOnPage(), CommentListDataModel.class));
    }

    /**
     * GET /comments/{id}
     * Retrieves comment identified by given ID.
     *
     * @param commentID ID of comment to retrieve
     * @return comment data
     * @throws ResourceNotFoundException if no comment found associated with given ID
     */
    @RequestMapping(method = RequestMethod.GET, path = PATH_PART_ID)
    @Timed
    public ResponseEntity<ExtendedCommentDataModel> getCommentById(@PathVariable(PATH_VARIABLE_ID) Long commentID)
            throws ResourceNotFoundException {

        try {
            CommentVO commentVO = commentFacade.getOne(commentID);

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(commentVO, ExtendedCommentDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_COMMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_COMMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * POST /comments
     * Creates a new comment.
     *
     * @param commentCreateRequestModel comment data
     * @param bindingResult validation result
     * @return created comment data
     * @throws RequestCouldNotBeFulfilledException if a service exception occurred
     */
    @RequestMapping(method = RequestMethod.POST)
    @Timed
    public ResponseEntity<BaseBodyDataModel> createComment(@RequestBody @AuthenticatedRequest @Valid CommentCreateRequestModel commentCreateRequestModel,
                                                           BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                Long commentID = commentFacade.createOne(conversionService.convert(commentCreateRequestModel, CommentVO.class));
                CommentVO commentVO = commentFacade.getOne(commentID);

                return ResponseEntity
                        .created(buildLocation(commentID))
                        .body(conversionService.convert(commentVO, CommentDataModel.class));
            } catch (ConstraintViolationException e) {
                LOGGER.error(ENTRY_TO_ASSOCIATE_COMMENT_WITH_COULD_NOT_BE_FOUND, e);
                throw new RequestCouldNotBeFulfilledException(YOUR_COMMENT_COULD_NOT_BE_CREATED);
            } catch (ServiceException e) {
                LOGGER.error(COMMENT_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(YOUR_COMMENT_COULD_NOT_BE_CREATED);
            }
        }
    }

    /**
     * PUT /comments/{id}
     * Updates given comment.
     *
     * @param commentID ID of the comment to update
     * @param commentUpdateRequestModel new comment data
     * @param bindingResult validation result
     * @return updated comment data
     * @throws ResourceNotFoundException if no comment found associated with given ID
     * @throws RequestCouldNotBeFulfilledException if a service exception occurred
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_PART_ID)
    public ResponseEntity<BaseBodyDataModel> updateComment(@PathVariable(PATH_VARIABLE_ID) Long commentID,
                                      @RequestBody @Valid CommentUpdateRequestModel commentUpdateRequestModel,
                                      BindingResult bindingResult)
            throws ResourceNotFoundException, RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                commentFacade.updateOne(commentID, conversionService.convert(commentUpdateRequestModel, CommentVO.class));
                CommentVO commentVO = commentFacade.getOne(commentID);

                return ResponseEntity
                        .created(buildLocation(commentID))
                        .body(conversionService.convert(commentVO, CommentDataModel.class));
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_COMMENT_NOT_FOUND, e);
                throw new ResourceNotFoundException(THE_COMMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
            }
        }
    }

    /**
     * PUT /comment/{id}/status
     * Changes comment status (enabled/disabled).
     *
     * @param commentID ID of the comment to change status of
     * @return updated comment data
     * @throws ResourceNotFoundException if no comment found associated with given ID
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_CHANGE_STATUS)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExtendedCommentDataModel> changeCommentStatus(@PathVariable(PATH_VARIABLE_ID) Long commentID)
            throws ResourceNotFoundException {

        try {
            CommentVO commentVO = commentFacade.getOne(commentID);
            if (commentVO.isEnabled()) {
                commentFacade.disable(commentID);
            } else {
                commentFacade.enable(commentID);
            }
            CommentVO updatedCommentVO = commentFacade.getOne(commentID);

            return ResponseEntity
                    .created(buildLocation(commentID))
                    .body(conversionService.convert(updatedCommentVO, ExtendedCommentDataModel.class));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_COMMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_COMMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * DELETE /comments/{id}
     * Logically deletes a comment. Self-moderation function for registered users; can be reverted by admin.
     *
     * @param commentID ID of the comment to logically delete
     * @throws ResourceNotFoundException if no comment found associated with given ID
     */
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_PART_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    public void deleteCommentLogically(@PathVariable(PATH_VARIABLE_ID) Long commentID)
            throws ResourceNotFoundException {

        try {
            CommentVO commentVO = commentFacade.getOne(commentID);
            commentFacade.deleteLogicallyByEntity(commentVO);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_COMMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_COMMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * DELETE /comments/{id}/permanent
     * Permanently deletes a comment. Only for admins.
     *
     * @param commentID ID of the comment to permanently delete
     * @throws ResourceNotFoundException if no comment found associated with given ID
     */
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_PERMANENT_DELETION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentPermanently(@PathVariable(PATH_VARIABLE_ID) Long commentID)
            throws ResourceNotFoundException {

        try {
            CommentVO commentVO = commentFacade.getOne(commentID);
            commentFacade.deleteByEntity(commentVO);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_COMMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_COMMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_COMMENTS + "/" + id);
    }
}
