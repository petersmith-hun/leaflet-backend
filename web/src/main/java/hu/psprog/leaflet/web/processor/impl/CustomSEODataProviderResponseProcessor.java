package hu.psprog.leaflet.web.processor.impl;

import hu.psprog.leaflet.service.vo.CustomSEODataProviderVO;
import hu.psprog.leaflet.web.processor.ResponseProcessor;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * {@link ResponseProcessor} implementation to extract custom SEO information.
 *
 * @author Peter Smith
 */
@Component
public class CustomSEODataProviderResponseProcessor implements ResponseProcessor<CustomSEODataProviderVO> {

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public CustomSEODataProviderResponseProcessor(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Extracts custom SEO values from {@link CustomSEODataProviderVO} value object.
     *
     * @param response service response of type {@link CustomSEODataProviderVO}
     */
    @Override
    public void process(CustomSEODataProviderVO response) {
        httpServletRequest.setAttribute(RequestParameter.SEO_META_TITLE, response.getSeoTitle());
        httpServletRequest.setAttribute(RequestParameter.SEO_META_DESCRIPTION, response.getSeoDescription());
        httpServletRequest.setAttribute(RequestParameter.SEO_META_KEYWORDS, response.getSeoKeywords());
    }
}
