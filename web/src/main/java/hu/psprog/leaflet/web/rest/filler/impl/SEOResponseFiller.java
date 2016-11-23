package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import hu.psprog.leaflet.web.rest.filler.ResponseFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Extends JSON answer with SEO parameters.
 *
 * @author Peter Smith
 */
@Component
public class SEOResponseFiller implements ResponseFiller {

    private static final String RESPONSE_NODE_SEO = "seo";
    private static final String RESPONSE_PARAMETER_PAGE_TITLE = "pageTitle";
    private static final String RESPONSE_PARAMETER_META_TITLE = "metaTitle";
    private static final String RESPONSE_PARAMETER_META_DESCRIPTION = "metaDescription";
    private static final String RESPONSE_PARAMETER_META_KEYWORDS = "metaKeywords";
    private static final String EMPTY_STRING = "";

    @Autowired
    private DynamicConfigurationPropertyService dynamicConfigurationPropertyService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void fill(ModelAndView modelAndView) {
        Map<String, String> seoParameters = new HashMap<>();
        seoParameters.put(RESPONSE_PARAMETER_PAGE_TITLE, getParameter(RequestParameter.SEO_PAGE_TITLE));
        seoParameters.put(RESPONSE_PARAMETER_META_TITLE, getParameter(RequestParameter.SEO_META_TITLE));
        seoParameters.put(RESPONSE_PARAMETER_META_DESCRIPTION, getParameter(RequestParameter.SEO_META_DESCRIPTION));
        seoParameters.put(RESPONSE_PARAMETER_META_KEYWORDS, getParameter(RequestParameter.SEO_META_KEYWORDS));
        modelAndView.addObject(RESPONSE_NODE_SEO, seoParameters);
    }

    @Override
    public boolean shouldFill() {
        return !((boolean) httpServletRequest.getAttribute(RequestParameter.IS_AJAX_REQUEST));
    }

    private String getParameter(String key) {

        return Stream.of(httpServletRequest.getAttribute(key), dynamicConfigurationPropertyService.get(key))
                .filter(Objects::nonNull)
                .findFirst()
                    .map(String::valueOf)
                    .orElse(EMPTY_STRING);
    }
}
