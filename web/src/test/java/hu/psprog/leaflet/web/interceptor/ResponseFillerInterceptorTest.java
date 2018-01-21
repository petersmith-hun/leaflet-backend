package hu.psprog.leaflet.web.interceptor;

import hu.psprog.leaflet.web.annotation.FillResponse;
import hu.psprog.leaflet.web.annotation.ResponseFillMode;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ResponseFillerInterceptor}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class ResponseFillerInterceptorTest {

    private HttpServletRequest request;

    @InjectMocks
    private ResponseFillerInterceptor responseFillerInterceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        request = new MockHttpServletRequest();
    }

    @Test
    @Parameters(source = AJAXParameterProvider.class)
    public void shouldPutAJAXFlag(String methodName, boolean expected) throws Exception {

        // given
        HandlerMethod handlerMethod = prepareHandlerMethod(methodName);

        // when
        responseFillerInterceptor.preHandle(request, null, handlerMethod);

        // then
        assertThat(getAJAXParameter(), is(expected));
    }

    @Test
    public void shouldNotChangeAJAXFlagIfAlreadySet() throws Exception {

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

    public static class AJAXParameterProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {"testMethodWithFillResponseInAJAXFillMode", true},
                    new Object[] {"testMethodWithFillResponseInDefaultFillMode", false},
                    new Object[] {"testMethodWithoutFillResponse", false}
            };
        }
    }
}