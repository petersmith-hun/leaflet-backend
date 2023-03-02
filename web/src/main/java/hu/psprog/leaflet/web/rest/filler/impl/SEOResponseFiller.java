package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.api.rest.response.common.SEODataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Extends JSON answer with SEO parameters.
 *
 * @author Peter Smith
 */
@Component
public class SEOResponseFiller extends AbstractAjaxRequestAwareResponseFiller {

    private final DynamicConfigurationPropertyService dynamicConfigurationPropertyService;

    @Autowired
    public SEOResponseFiller(DynamicConfigurationPropertyService dynamicConfigurationPropertyService, HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
        this.dynamicConfigurationPropertyService = dynamicConfigurationPropertyService;
    }

    @Override
    public void fill(WrapperBodyDataModel.WrapperBodyDataModelBuilder<?> wrapperBodyDataModelBuilder) {

        SEODataModel seoDataModel = SEODataModel.getBuilder()
                .withPageTitle(getParameter(RequestParameter.SEO_PAGE_TITLE))
                .withMetaTitle(getParameter(RequestParameter.SEO_META_TITLE))
                .withMetaDescription(getParameter(RequestParameter.SEO_META_DESCRIPTION))
                .withMetaKeywords(getParameter(RequestParameter.SEO_META_KEYWORDS))
                .build();

        wrapperBodyDataModelBuilder.withSeo(seoDataModel);
    }

    private String getParameter(String key) {

        return Stream.of(httpServletRequest.getAttribute(key), dynamicConfigurationPropertyService.get(key))
                .filter(Objects::nonNull)
                .findFirst()
                .map(String::valueOf)
                .orElse(StringUtils.EMPTY);
    }
}
