package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import hu.psprog.leaflet.web.rest.filler.ResponseFiller;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Abstract implementation of {@link ResponseFiller} interface.
 * Implements shouldFill method with a default behavior of checking if current {@link HttpServletRequest} is marked as AJAX.
 *
 * @author Peter Smith
 */
abstract class AbstractAjaxRequestAwareResponseFiller implements ResponseFiller {

    HttpServletRequest httpServletRequest;

    AbstractAjaxRequestAwareResponseFiller(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean shouldFill() {

        return Optional.ofNullable(httpServletRequest.getAttribute(RequestParameter.IS_AJAX_REQUEST))
                .map(attribute -> !Boolean.parseBoolean(attribute.toString()))
                .orElse(true);
    }
}
