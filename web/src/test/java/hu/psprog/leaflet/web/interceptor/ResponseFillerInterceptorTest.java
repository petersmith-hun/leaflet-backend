package hu.psprog.leaflet.web.interceptor;

import hu.psprog.leaflet.web.annotation.FillResponse;
import hu.psprog.leaflet.web.annotation.ResponseFillMode;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ResponseFillerInterceptor}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ResponseFillerInterceptorTest {

    private HttpServletRequest request;

    @InjectMocks
    private ResponseFillerInterceptor responseFillerInterceptor;

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
    }

    @ParameterizedTest
    @MethodSource("preHandleDataProvider")
    public void shouldPutAJAXFlag(String methodName, boolean expected) {

        // given
        HandlerMethod handlerMethod = prepareHandlerMethod(methodName);

        // when
        responseFillerInterceptor.preHandle(request, null, handlerMethod);

        // then
        assertThat(getAJAXParameter(), is(expected));
    }

    @Test
    public void shouldNotChangeAJAXFlagIfAlreadySet() {

        // given
        request.setAttribute(RequestParameter.IS_AJAX_REQUEST, true);
        HandlerMethod handlerMethod = prepareHandlerMethod("testMethodWithFillResponseInDefaultFillMode");

        // when
        responseFillerInterceptor.preHandle(request, null, handlerMethod);

        // then
        assertThat(getAJAXParameter(), is(true));
    }

    private Boolean getAJAXParameter() {
        return Boolean.valueOf(request.getAttribute(RequestParameter.IS_AJAX_REQUEST).toString());
    }

    private HandlerMethod prepareHandlerMethod(String methodName) {
        return new HandlerMethod(this, ReflectionUtils.findMethod(ResponseFillerInterceptorTest.class, methodName));
    }

    @FillResponse(fill = ResponseFillMode.AJAX)
    private void testMethodWithFillResponseInAJAXFillMode() {
    }

    @FillResponse
    private void testMethodWithFillResponseInDefaultFillMode() {
    }

    private void testMethodWithoutFillResponse() {
    }

    private static Stream<Arguments> preHandleDataProvider() {

        return Stream.of(
                Arguments.of("testMethodWithFillResponseInAJAXFillMode", true),
                Arguments.of("testMethodWithFillResponseInDefaultFillMode", false),
                Arguments.of("testMethodWithoutFillResponse", false)
        );
    }
}