package hu.psprog.leaflet.web.rest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.web.metrics.ExceptionHandlerCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Authentication exception handler for REST request.
 *
 * @author Peter Smith
 */
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint, AuthenticationFailureHandler {

    private static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
    private static final String CONTENT_TYPE = "application/json";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExceptionHandlerCounters exceptionHandlerCounters;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        handleFailure(response);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        handleFailure(response);
    }

    private void handleFailure(HttpServletResponse response) throws IOException {

        exceptionHandlerCounters.authenticationFailure();
        PrintWriter responseWriter = response.getWriter();
        ErrorMessageDataModel errorMessageDataModel = ErrorMessageDataModel.getBuilder()
                .withMessage(UNAUTHORIZED_ACCESS)
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);
        responseWriter.println(objectMapper.writeValueAsString(errorMessageDataModel));
    }
}
