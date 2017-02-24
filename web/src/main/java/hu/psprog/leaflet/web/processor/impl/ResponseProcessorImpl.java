package hu.psprog.leaflet.web.processor.impl;

import hu.psprog.leaflet.service.vo.BaseVO;
import hu.psprog.leaflet.service.vo.CustomSEODataProviderVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.web.processor.ResponseProcessor;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link ResponseProcessor}.
 * {@inheritDoc}
 *
 * @author Peter Smith
 */
@Component
public class ResponseProcessorImpl implements ResponseProcessor {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public <T extends BaseVO> void process(EntityPageVO<T> response) {

        httpServletRequest.setAttribute(RequestParameter.PAGINATION_ENTITY_COUNT, response.getEntityCount());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_ENTITY_COUNT_ON_PAGE, response.getEntityCountOnPage());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_PAGE_COUNT, response.getPageCount());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_PAGE_NUMBER, response.getPageNumber());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_IS_FIRST, response.isFirst());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_IS_LAST, response.isLast());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_HAS_NEXT, response.hasNext());
        httpServletRequest.setAttribute(RequestParameter.PAGINATION_HAS_PREVIOUS, response.hasPrevious());
    }

    @Override
    public <T extends BaseVO> void process(CustomSEODataProviderVO<T> response) {

        httpServletRequest.setAttribute(RequestParameter.SEO_META_TITLE, response.getSEOTitle());
        httpServletRequest.setAttribute(RequestParameter.SEO_META_DESCRIPTION, response.getSEODescription());
        httpServletRequest.setAttribute(RequestParameter.SEO_META_KEYWORDS, response.getSEOKeywords());
    }
}
