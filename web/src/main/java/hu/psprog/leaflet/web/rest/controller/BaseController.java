package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Base REST controller.
 *
 * @author Peter Smith
 */
@ControllerAdvice
@ResponseBody
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    /**
     * HTTP 404 exception handler.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageDataModel resourceNotFoundExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Requested resource is not available.", exception);

        return new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build();
    }

    /**
     * Temporary exception handler, specific handlers shall be created later!
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageDataModel defaultExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Default handler caught exception.", exception);

        return new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build();
    }
}
