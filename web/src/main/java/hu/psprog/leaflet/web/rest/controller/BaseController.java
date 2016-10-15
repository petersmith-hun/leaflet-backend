package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
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

    /**
     * Temporary exception handler, specific handlers shall be created later!
     */
    @ExceptionHandler(Exception.class)
    public ErrorMessageDataModel defaultExceptionHandler(HttpServletRequest request, Exception exception) {

        return new ErrorMessageDataModel.Builder()
                .withMessage(exception.getMessage())
                .build();
    }
}
