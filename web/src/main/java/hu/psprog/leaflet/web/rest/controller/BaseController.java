package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.web.exception.AuthenticationFailureException;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

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
    static final String BASE_PATH_FILES = "/files";
    static final String BASE_PATH_ATTACHMENTS = "/attachments";
    static final String BASE_PATH_TAGS = "/tags";

    static final String PATH_VARIABLE_ID = "id";
    static final String PATH_VARIABLE_PAGE = "page";
    static final String PATH_VARIABLE_LINK = "link";
    static final String PATH_VARIABLE_FILENAME = "storedFilename";
    static final String PATH_VARIABLE_FILE_IDENTIFIER = "fileIdentifier";

    static final String PATH_PART_ID = "/{id}";
    static final String PATH_PART_PAGE = "/{page}";
    static final String PATH_PART_LINK = "/{link}";
    static final String PATH_PART_FILENAME = "/{storedFilename}";
    static final String PATH_PART_FILE_IDENTIFIER = "/{fileIdentifier}";

    static final String PATH_CHANGE_STATUS = PATH_PART_ID + "/status";
    static final String PATH_PUBLIC = "/public";

    static final String REQUEST_PARAMETER_LIMIT = "limit";
    static final String REQUEST_PARAMETER_ORDER_BY = "orderBy";
    static final String REQUEST_PARAMETER_ORDER_DIRECTION = "orderDirection";

    static final String PAGINATION_DEFAULT_LIMIT = "10";
    static final String PAGINATION_DEFAULT_ORDER_BY = "CREATED";
    static final String PAGINATION_DEFAULT_ORDER_DIRECTION = "ASC";

    static final String CONSTRAINT_VIOLATION = "Constraint violation";

    @Autowired
    ConversionService conversionService;

    /**
     * HTTP 404 exception handler.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDataModel> resourceNotFoundExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Requested resource is not available.", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }

    /**
     * HTTP 401 exception handler.
     */
    @ExceptionHandler({AuthenticationFailureException.class, AuthenticationException.class})
    public ResponseEntity<ErrorMessageDataModel> authenticationFailureExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Exception thrown during user authentication.", exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }

    /**
     * HTTP 409 exception handler.
     */
    @ExceptionHandler(RequestCouldNotBeFulfilledException.class)
    public ResponseEntity<ErrorMessageDataModel> requestCouldNotBeFulfilledExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("User interaction caused a recognized exception.", exception);

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
    public ResponseEntity<ErrorMessageDataModel> defaultExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Default handler caught exception.", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessageDataModel.getBuilder()
                        .withMessage(exception.getMessage())
                        .build());
    }
}
