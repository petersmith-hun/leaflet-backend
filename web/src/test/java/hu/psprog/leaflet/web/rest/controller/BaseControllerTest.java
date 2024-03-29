package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link BaseController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class BaseControllerTest extends AbstractControllerBaseTest {

    private static final String ERROR_MESSAGE = "error message";
    private static final Exception EXCEPTION = new Exception(ERROR_MESSAGE);
    private static final ErrorMessageDataModel ERROR_MESSAGE_DATA_MODEL = ErrorMessageDataModel.getBuilder()
            .withMessage(ERROR_MESSAGE)
            .build();

    @InjectMocks
    private BaseController baseController;

    @Test
    public void shouldHandleResourceNotFoundException() {

        // when
        ResponseEntity<ErrorMessageDataModel> result = baseController.resourceNotFoundExceptionHandler(EXCEPTION);

        // then
        assertResponse(result, HttpStatus.NOT_FOUND, ERROR_MESSAGE_DATA_MODEL);
        verify(exceptionHandlerCounters).resourceNotFound();
    }

    @Test
    public void shouldHandleAuthenticationFailureException() {

        // when
        ResponseEntity<ErrorMessageDataModel> result = baseController.authenticationFailureExceptionHandler(EXCEPTION);

        // then
        assertResponse(result, HttpStatus.UNAUTHORIZED, ERROR_MESSAGE_DATA_MODEL);
        verify(exceptionHandlerCounters).authenticationFailure();
    }

    @Test
    public void shouldHandleAuthorizationFailureException() {

        // when
        ResponseEntity<ErrorMessageDataModel> result = baseController.authorizationFailureExceptionHandler(EXCEPTION);

        // then
        assertResponse(result, HttpStatus.FORBIDDEN, ERROR_MESSAGE_DATA_MODEL);
        verify(exceptionHandlerCounters).authorizationFailure();
    }

    @Test
    public void shouldHandleRequestCouldNotBeFulfilledException() {

        // when
        ResponseEntity<ErrorMessageDataModel> result = baseController.requestCouldNotBeFulfilledExceptionHandler(EXCEPTION);

        // then
        assertResponse(result, HttpStatus.CONFLICT, ERROR_MESSAGE_DATA_MODEL);
        verify(exceptionHandlerCounters).requestCouldNotBeFulfilled();
    }

    @Test
    public void shouldHandleAnyOtherExceptions() {

        // when
        ResponseEntity<ErrorMessageDataModel> result = baseController.defaultExceptionHandler(EXCEPTION);

        // then
        assertResponse(result, HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE_DATA_MODEL);
        verify(exceptionHandlerCounters).defaultExceptionHandler();
    }
}