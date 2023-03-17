package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.metrics.ExceptionHandlerCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handlers, common constants and operations.
 *
 * @author Peter Smith
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    static final String BASE_PATH_USERS = "/users";
    static final String BASE_PATH_ENTRIES = "/entries";
    static final String BASE_PATH_CATEGORIES = "/categories";
    static final String BASE_PATH_DCP = "/dcp";
    static final String BASE_PATH_DOCUMENTS = "/documents";
    static final String BASE_PATH_COMMENTS = "/comments";
    static final String BASE_PATH_ATTACHMENTS = "/attachments";
    static final String BASE_PATH_TAGS = "/tags";
    static final String BASE_PATH_ROUTES = "/routes";
    static final String BASE_PATH_SITEMAP = "/sitemap.xml";
    static final String BASE_PATH_CONTACT = "/contact";

    static final String PATH_VARIABLE_ID = "id";
    static final String PATH_VARIABLE_PAGE = "page";
    static final String PATH_VARIABLE_LINK = "link";

    static final String PATH_PART_ID = "/{id}";
    static final String PATH_PART_PAGE = "/{page}";
    static final String PATH_PART_LINK = "/{link}";

    static final String PATH_CHANGE_STATUS = PATH_PART_ID + "/status";
    static final String PATH_CHANGE_PUBLICATION_STATUS = PATH_PART_ID + "/publication/{status}";
    static final String PATH_PUBLIC = "/public";

    static final String REQUEST_PARAMETER_CONTENT = "content";
    static final String REQUEST_PARAMETER_LIMIT = "limit";
    static final String REQUEST_PARAMETER_ORDER_BY = "orderBy";
    static final String REQUEST_PARAMETER_ORDER_DIRECTION = "orderDirection";

    static final String PAGINATION_DEFAULT_LIMIT = "10";
    static final String PAGINATION_DEFAULT_ORDER_BY = "CREATED";
    static final String PAGINATION_DEFAULT_ORDER_DIRECTION = "ASC";

    static final String CONSTRAINT_VIOLATION = "Constraint violation";

    protected final ConversionService conversionService;
    private final ExceptionHandlerCounters exceptionHandlerCounters;

    protected BaseController(ConversionService conversionService, ExceptionHandlerCounters exceptionHandlerCounters) {
        this.conversionService = conversionService;
        this.exceptionHandlerCounters = exceptionHandlerCounters;
    }

    /**
     * HTTP 404 exception handler.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDataModel> resourceNotFoundExceptionHandler(Exception exception) {

        LOGGER.error("Requested resource is not available.", exception);
        exceptionHandlerCounters.resourceNotFound();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }

    /**
     * HTTP 401 exception handler.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessageDataModel> authenticationFailureExceptionHandler(Exception exception) {

        LOGGER.error("Exception thrown during user authentication.", exception);
        exceptionHandlerCounters.authenticationFailure();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }

    /**
     * HTTP 403 exception handler.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessageDataModel> authorizationFailureExceptionHandler(Exception exception) {

        LOGGER.error("Unauthorized operation attempt was made by user.", exception);
        exceptionHandlerCounters.authorizationFailure();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }

    /**
     * HTTP 409 exception handler.
     */
    @ExceptionHandler(RequestCouldNotBeFulfilledException.class)
    public ResponseEntity<ErrorMessageDataModel> requestCouldNotBeFulfilledExceptionHandler(Exception exception) {

        LOGGER.error("User interaction caused a recognized exception.", exception);
        exceptionHandlerCounters.requestCouldNotBeFulfilled();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }

    /**
     * Temporary exception handler, specific handlers shall be created later!
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDataModel> defaultExceptionHandler(Exception exception) {

        LOGGER.error("Default handler caught exception.", exception);
        exceptionHandlerCounters.defaultExceptionHandler();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }

    /**
     * Builds validation failure response.
     *
     * @param bindingResult validation results
     * @return ValidationErrorMessageListDataModel wrapped in {@link ResponseEntity} with HTTP status 400
     */
    ResponseEntity<BaseBodyDataModel> validationFailureResponse(BindingResult bindingResult) {
        return ResponseEntity
                .badRequest()
                .body(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
    }
}
