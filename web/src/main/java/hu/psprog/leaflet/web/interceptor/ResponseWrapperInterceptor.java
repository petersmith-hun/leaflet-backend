package hu.psprog.leaflet.web.interceptor;

import hu.psprog.leaflet.web.annotation.AJAXRequest;
import hu.psprog.leaflet.web.config.WebMVCConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor to decide whether response should be automatically wrapped into {@link hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel} model or not.
 *
 * @author Peter Smith
 */
public class ResponseWrapperInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        AJAXRequest ajaxRequest = ((HandlerMethod) handler).getMethodAnnotation(AJAXRequest.class);
        request.setAttribute(WebMVCConfiguration.IS_AJAX_REQUEST, ajaxRequest != null);

        return super.preHandle(request, response, handler);
    }
}
