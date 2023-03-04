package hu.psprog.leaflet.web.processor.impl;

import hu.psprog.leaflet.service.vo.BaseVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.web.processor.ResponseProcessor;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * {@link ResponseProcessor} implementation to extract pagination information.
 *
 * @author Peter Smith
 */
@Component
public class EntityPageResponseProcessor implements ResponseProcessor<EntityPageVO<? extends BaseVO>> {

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public EntityPageResponseProcessor(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Extracts pagination information from {@link EntityPageVO} value object.
     *
     * @param response service response of type {@link EntityPageVO}
     */
    @Override
    public void process(EntityPageVO<? extends BaseVO> response) {
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_ENTITY_COUNT, response.getEntityCount());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_ENTITY_COUNT_ON_PAGE, response.getEntityCountOnPage());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_PAGE_COUNT, response.getPageCount());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_PAGE_NUMBER, response.getPageNumber());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_IS_FIRST, response.isFirst());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_IS_LAST, response.isLast());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_HAS_NEXT, response.hasNext());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_HAS_PREVIOUS, response.hasPrevious());
    }
}
