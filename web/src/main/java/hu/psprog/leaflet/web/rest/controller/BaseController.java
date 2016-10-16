package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * Temporary exception handler, specific handlers shall be created later!
     */
    @ExceptionHandler(Exception.class)
    public ErrorMessageDataModel defaultExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Default handler caught exception.", exception);

        return new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build();
    }
}
