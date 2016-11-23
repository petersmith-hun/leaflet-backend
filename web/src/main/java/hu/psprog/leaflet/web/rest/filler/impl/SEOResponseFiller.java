package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.web.rest.filler.ResponseFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Extends JSON answer with SEO parameters.
 *
 * @author Peter Smith
 */
@Component
public class SEOResponseFiller implements ResponseFiller {

    private static final String IS_AJAX_REQUEST = "isAjaxRequest";

    @Autowired
    private DynamicConfigurationPropertyService dynamicConfigurationPropertyService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void fill(ModelAndView modelAndView) {
        Map<String, String> seoParameters = new HashMap<>();
        seoParameters.put("pageTitle", "Page Title");
        seoParameters.put("metaTitle", "Meta Title");
        seoParameters.put("metaDescription", "Meta Description");
        seoParameters.put("metaKeywords", "Meta Keywords");
        modelAndView.addObject("seo", seoParameters);
    }

    @Override
    public boolean shouldFill() {
        return Objects.isNull(httpServletRequest.getAttribute(IS_AJAX_REQUEST));
    }
}
