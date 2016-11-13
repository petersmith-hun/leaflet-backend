package hu.psprog.leaflet.web.interceptor;

import hu.psprog.leaflet.web.annotation.AJAXRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Interceptor to wrap answer into "layout". Technically it extends the response with common sections (SEO values, menus, etc.) if required.
 *
 * @author Peter Smith
 */
@Component
public class ResponseWrapperInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        AJAXRequest ajaxRequest = ((HandlerMethod) handler).getMethodAnnotation(AJAXRequest.class);
        if (Objects.isNull(ajaxRequest)) {
            // TODO add common sections
        }

        super.postHandle(request, response, handler, modelAndView);
    }
}
