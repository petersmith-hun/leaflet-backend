package hu.psprog.leaflet.web.interceptor;

import hu.psprog.leaflet.web.annotation.AJAXRequest;
import hu.psprog.leaflet.web.rest.filler.ResponseFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * Interceptor to wrap answer into "layout". Technically it extends the response with common sections (SEO values, menus, etc.) if required.
 *
 * @author Peter Smith
 */
@Component
public class ResponseFillerInterceptor extends HandlerInterceptorAdapter {

    private static final String IS_AJAX_REQUEST = "isAjaxRequest";

    @Autowired
    private List<ResponseFiller> responseFillers;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        AJAXRequest ajaxRequest = ((HandlerMethod) handler).getMethodAnnotation(AJAXRequest.class);
        request.setAttribute(IS_AJAX_REQUEST, Objects.nonNull(ajaxRequest));

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        responseFillers.stream()
                .filter(ResponseFiller::shouldFill)
                .forEach(responseFiller -> responseFiller.fill(modelAndView));

        super.postHandle(request, response, handler, modelAndView);
    }
}
