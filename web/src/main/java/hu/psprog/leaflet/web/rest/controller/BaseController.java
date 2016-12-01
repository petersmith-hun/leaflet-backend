package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.web.exception.AuthenticationFailureException;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception handlers, common constants and operations.
 *
 * @author Peter Smith
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private static final String BODY = "body";
    private static final String ERROR = "error";
    private static final String REQUEST_PARAMETER_PAGINATION = "pagination";

    static final String BASE_PATH_USERS = "/users";
    static final String BASE_PATH_ENTRIES = "/entries";
    static final String BASE_PATH_CATEGORIES = "/categories";
    static final String BASE_PATH_DCP = "/dcp";

    static final String PATH_VARIABLE_ID = "id";
    static final String PATH_VARIABLE_PAGE = "page";
    static final String PATH_VARIABLE_LINK = "link";

    static final String PATH_PART_ID = "/{id}";
    static final String PATH_PART_PAGE = "/{page}";
    static final String PATH_PART_LINK = "/{link}";

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
    protected HttpServletRequest httpServletRequest;

    /**
     * HTTP 404 exception handler.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView resourceNotFoundExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Requested resource is not available.", exception);

        return wrap(new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build());
    }

    /**
     * HTTP 401 exception handler.
     */
    @ExceptionHandler({AuthenticationFailureException.class, AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView authenticationFailureExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Exception thrown during user authentication.", exception);

        return wrap(new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build());
    }

    /**
     * HTTP 409 exception handler.
     */
    @ExceptionHandler(RequestCouldNotBeFulfilledException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView requestCouldNotBeFulfilledExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("User interaction caused a recognized exception.", exception);

        return wrap(new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build());
    }

    /**
     * Temporary exception handler, specific handlers shall be created later!
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Default handler caught exception.", exception);

        return wrap(new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build());
    }

    /**
     * Wraps controllers validation/request error answer into {@link ModelAndView} object.
     * Answer will be stored under ERROR key.
     *
     * @param answer raw answer of controller
     * @return wrapped answer
     */
    protected ModelAndView wrap(ErrorMessageDataModel answer) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(ERROR, answer);

        return modelAndView;
    }

    /**
     * Wraps controllers answer into {@link ModelAndView} object.
     * Answer will be stored under BODY key.
     *
     * @param answer raw answer of controller
     * @return wrapped answer
     */
    protected ModelAndView wrap(BaseBodyDataModel answer) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(BODY, answer);

        return modelAndView;
    }
}
