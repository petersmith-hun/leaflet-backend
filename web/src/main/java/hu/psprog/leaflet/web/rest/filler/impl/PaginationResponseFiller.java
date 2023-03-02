package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.api.rest.response.common.PaginationDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import hu.psprog.leaflet.web.rest.filler.ResponseFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Extends JSON answer with pagination parameters.
 *
 * @author Peter Smith
 */
@Component
public class PaginationResponseFiller implements ResponseFiller {

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public PaginationResponseFiller(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void fill(WrapperBodyDataModel.WrapperBodyDataModelBuilder<?> wrapperBodyDataModelBuilder) {

        PaginationDataModel paginationDataModel = PaginationDataModel.getBuilder()
                .withEntityCount((long) httpServletRequest.getAttribute(RequestParameter.PAGINATION_ENTITY_COUNT))
                .withEntityCountOnPage((int) httpServletRequest.getAttribute(RequestParameter.PAGINATION_ENTITY_COUNT_ON_PAGE))
                .withPageCount((int) httpServletRequest.getAttribute(RequestParameter.PAGINATION_PAGE_COUNT))
                .withPageNumber((int) httpServletRequest.getAttribute(RequestParameter.PAGINATION_PAGE_NUMBER))
                .withFirst((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_IS_FIRST))
                .withLast((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_IS_LAST))
                .withHasNext((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_HAS_NEXT))
                .withHasPrevious((boolean) httpServletRequest.getAttribute(RequestParameter.PAGINATION_HAS_PREVIOUS))
                .build();

        wrapperBodyDataModelBuilder.withPagination(paginationDataModel);
    }

    @Override
    public boolean shouldFill() {
        return Objects.nonNull(httpServletRequest.getAttribute(RequestParameter.PAGINATION_ENTITY_COUNT));
    }
}
