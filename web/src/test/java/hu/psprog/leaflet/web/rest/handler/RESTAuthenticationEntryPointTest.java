package hu.psprog.leaflet.web.rest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link RESTAuthenticationEntryPoint}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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

    @InjectMocks
    private RESTAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Test
    public void shouldCommence() throws IOException {

        // given
        given(response.getWriter()).willReturn(printWriter);
        given(objectMapper.writeValueAsString(prepareErrorMessageDataModel())).willReturn(ERROR_MESSAGE_AS_JSON);

        // when
        restAuthenticationEntryPoint.commence(request, response, authenticationException);

        // then
        verify(response).setStatus(401);
        verify(response).setContentType(CONTENT_TYPE);
        verify(printWriter).println(ERROR_MESSAGE_AS_JSON);
    }

    private ErrorMessageDataModel prepareErrorMessageDataModel() {
        return ErrorMessageDataModel.getBuilder()
                .withMessage(UNAUTHORIZED_ACCESS)
                .build();
    }
}