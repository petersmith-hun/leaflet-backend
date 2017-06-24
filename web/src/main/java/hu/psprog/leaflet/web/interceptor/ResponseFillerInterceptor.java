package hu.psprog.leaflet.web.interceptor;

import hu.psprog.leaflet.web.annotation.FillResponse;
import hu.psprog.leaflet.web.annotation.ResponseFillMode;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * Interceptor to wrap answer into "layout". Technically it extends the response with common sections (SEO values, menus, etc.) if required.
 *
 * @author Peter Smith
 */
@Component
public class ResponseFillerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setAJAXFlag(request, getFillResponseAnnotation(handler));
        return super.preHandle(request, response, handler);
    }

    private FillResponse getFillResponseAnnotation(Object handler) {
        return ((HandlerMethod) handler).getMethodAnnotation(FillResponse.class);
    }

    private void setAJAXFlag(HttpServletRequest request, FillResponse fillResponseAnnotation) {
        if (Objects.isNull(request.getAttribute(RequestParameter.IS_AJAX_REQUEST))) {
            request.setAttribute(RequestParameter.IS_AJAX_REQUEST, isAJAXRequest(fillResponseAnnotation));
        }
    }

    private boolean isAJAXRequest(FillResponse fillResponseAnnotation) {
        return Optional.ofNullable(fillResponseAnnotation)
                .map(fillResponse -> fillResponse.fill() == ResponseFillMode.AJAX)
                .orElse(false);
    }
}
