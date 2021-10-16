package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.api.rest.response.common.SEODataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link SEOResponseFiller}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class SEOResponseFillerTest {

    private static final String DCP_PAGE_TITLE = "DCP Page title";
    private static final String DCP_SEO_TITLE = "DCP SEO title";
    private static final String DCP_SEO_DESCRIPTION = "DCP SEO Description";
    private static final String DCP_SEO_KEYWORDS = "DCP SEO Keywords";

    private static final String SEO_TITLE = "SEO title";
    private static final String SEO_DESCRIPTION = "SEO Description";
    private static final String SEO_KEYWORDS = "SEO Keywords";

    @Mock(lenient = true)
    private DynamicConfigurationPropertyService dcpService;

    private SEOResponseFiller responseFiller;

    @Test
    public void shouldFillResponseWithFullParameterSet() {

        // given
        prepareFillerMockWithFullParameterSet();
        WrapperBodyDataModel.WrapperBodyDataModelBuilder builder = WrapperBodyDataModel.getBuilder();

        // when
        responseFiller.fill(builder);

        // then
        assertThat(builder.build().getSeo(), equalTo(SEODataModel.getBuilder()
                .withPageTitle(DCP_PAGE_TITLE)
                .withMetaTitle(SEO_TITLE)
                .withMetaDescription(SEO_DESCRIPTION)
                .withMetaKeywords(SEO_KEYWORDS)
                .build()));
    }

    @Test
    public void shouldFillResponseWithPartialParameterSet() {

        // given
        prepareFillerMockWithPartialParameterSet();
        WrapperBodyDataModel.WrapperBodyDataModelBuilder builder = WrapperBodyDataModel.getBuilder();

        // when
        responseFiller.fill(builder);

        // then
        assertThat(builder.build().getSeo(), equalTo(SEODataModel.getBuilder()
                .withPageTitle(DCP_PAGE_TITLE)
                .withMetaTitle(SEO_TITLE)
                .withMetaDescription(DCP_SEO_DESCRIPTION)
                .withMetaKeywords(DCP_SEO_KEYWORDS)
                .build()));
    }

    @Test
    public void shouldFillResponseWithEmptyParameterSet() {

        // given
        prepareFillerMock(new MockHttpServletRequest());
        WrapperBodyDataModel.WrapperBodyDataModelBuilder builder = WrapperBodyDataModel.getBuilder();

        // when
        responseFiller.fill(builder);

        // then
        assertThat(builder.build().getSeo(), equalTo(SEODataModel.getBuilder()
                .withPageTitle(StringUtils.EMPTY)
                .withMetaTitle(StringUtils.EMPTY)
                .withMetaDescription(StringUtils.EMPTY)
                .withMetaKeywords(StringUtils.EMPTY)
                .build()));
    }

    @Test
    public void shouldFillNonAJAXRequest() {

        // given
        prepareFillerMockWithAJAXRequestParameter(false);

        // when
        boolean result = responseFiller.shouldFill();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldNotFillAJAXRequest() {

        // given
        prepareFillerMockWithAJAXRequestParameter(true);

        // when
        boolean result = responseFiller.shouldFill();

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldFillIfAJAXParameterIsMissing() {

        // given
        prepareFillerMock(new MockHttpServletRequest());

        // when
        boolean result = responseFiller.shouldFill();

        // then
        assertThat(result, is(true));
    }

    private void prepareFillerMockWithFullParameterSet() {

        HttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(RequestParameter.SEO_META_TITLE, SEO_TITLE);
        request.setAttribute(RequestParameter.SEO_META_DESCRIPTION, SEO_DESCRIPTION);
        request.setAttribute(RequestParameter.SEO_META_KEYWORDS, SEO_KEYWORDS);
        prepareFillerMock(request);
        prepareDCPServiceMock();
    }

    private void prepareFillerMockWithPartialParameterSet() {

        HttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(RequestParameter.SEO_META_TITLE, SEO_TITLE);
        prepareFillerMock(request);
        prepareDCPServiceMock();
    }

    private void prepareFillerMockWithAJAXRequestParameter(boolean value) {

        HttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(RequestParameter.IS_AJAX_REQUEST, value);
        prepareFillerMock(request);
    }

    private void prepareFillerMock(HttpServletRequest request) {
        responseFiller = new SEOResponseFiller(dcpService, request);
    }

    private void prepareDCPServiceMock() {
        given(dcpService.get(RequestParameter.SEO_PAGE_TITLE)).willReturn(DCP_PAGE_TITLE);
        given(dcpService.get(RequestParameter.SEO_META_TITLE)).willReturn(DCP_SEO_TITLE);
        given(dcpService.get(RequestParameter.SEO_META_DESCRIPTION)).willReturn(DCP_SEO_DESCRIPTION);
        given(dcpService.get(RequestParameter.SEO_META_KEYWORDS)).willReturn(DCP_SEO_KEYWORDS);
    }
}