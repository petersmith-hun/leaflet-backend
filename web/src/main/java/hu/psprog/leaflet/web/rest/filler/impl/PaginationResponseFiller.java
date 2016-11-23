package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.api.rest.response.common.PaginationDataModel;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
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

        PaginationDataModel paginationDataModel = new PaginationDataModel.Builder()
                .withEntityCount((long) httpServletRequest.getAttribute(RequestParameter.PAGINATION_ENTITY_COUNT))
                .withEntityCountOnPage((int) httpServletRequest.getAttribute(RequestParameter.PAGINATION_ENTITY_COUNT_ON_PAGE))
                .withPageCount((int) httpServletRequest.getAttribute(RequestParameter.PAGINATION_PAGE_COUNT))
                .withPageNumber((int) httpServletRequest.getAttribute(RequestParameter.PAGINATION_PAGE_NUMBER))
                .withIsFirst((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_IS_FIRST))
                .withIsLast((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_IS_LAST))
                .withHasNext((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_HAS_NEXT))
                .withHasPrevious((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_HAS_PREVIOUS))
                .build();

        modelAndView.addObject(PAGINATION, paginationDataModel);
    }

    @Override
    public boolean shouldFill() {
        return Objects.nonNull(httpServletRequest.getAttribute(PAGINATION));
    }
}
