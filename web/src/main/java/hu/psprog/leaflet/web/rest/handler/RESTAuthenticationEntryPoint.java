package hu.psprog.leaflet.web.rest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Authentication exception handler for REST request.
 *
 * @author Peter Smith
 */
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String UNAUTHORIZED_ACCESS = "Unauthorized access";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        PrintWriter responseWriter = response.getWriter();
        ErrorMessageDataModel errorMessageDataModel = new ErrorMessageDataModel.Builder()
                .withMessage(UNAUTHORIZED_ACCESS)
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        responseWriter.println(objectMapper.writeValueAsString(errorMessageDataModel));
    }
}
