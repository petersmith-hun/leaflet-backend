package hu.psprog.leaflet.web.rest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.web.metrics.ExceptionHandlerCounters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link RESTAuthenticationEntryPoint}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class RESTAuthenticationEntryPointTest {

    private static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
    private static final String CONTENT_TYPE = "application/json";
    private static final String ERROR_MESSAGE_AS_JSON = "error-message-as-json";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authenticationException;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private ExceptionHandlerCounters exceptionHandlerCounters;

    @InjectMocks
    private RESTAuthenticationEntryPoint restAuthenticationEntryPoint;

    @BeforeEach
    public void setup() throws IOException {

        // given
        given(response.getWriter()).willReturn(printWriter);
        given(objectMapper.writeValueAsString(prepareErrorMessageDataModel())).willReturn(ERROR_MESSAGE_AS_JSON);
    }

    @Test
    public void shouldCommence() throws IOException {

        // when
        restAuthenticationEntryPoint.commence(request, response, authenticationException);

        // then
        verifyAll();
    }

    @Test
    public void shouldHandleFailure() throws IOException {

        // when
        restAuthenticationEntryPoint.onAuthenticationFailure(request, response, authenticationException);

        // then
        verifyAll();
    }

    private void verifyAll() {
        verify(response).setStatus(401);
        verify(response).setContentType(CONTENT_TYPE);
        verify(printWriter).println(ERROR_MESSAGE_AS_JSON);
        verify(exceptionHandlerCounters).authenticationFailure();
    }

    private ErrorMessageDataModel prepareErrorMessageDataModel() {
        return ErrorMessageDataModel.getBuilder()
                .withMessage(UNAUTHORIZED_ACCESS)
                .build();
    }
}