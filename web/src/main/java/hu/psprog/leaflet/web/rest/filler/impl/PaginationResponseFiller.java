package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.web.rest.filler.ResponseFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Extends JSON answer with pagination parameters.
 *
 * @author Peter Smith
 */
@Component
public class PaginationResponseFiller implements ResponseFiller {

    private static final String PAGINATION = "pagination";

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void fill(ModelAndView modelAndView) {
        modelAndView.addObject(PAGINATION, httpServletRequest.getAttribute(PAGINATION));
    }

    @Override
    public boolean shouldFill() {
        return Objects.nonNull(httpServletRequest.getAttribute(PAGINATION));
    }
}
