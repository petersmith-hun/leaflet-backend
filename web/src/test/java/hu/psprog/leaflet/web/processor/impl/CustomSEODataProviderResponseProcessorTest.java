package hu.psprog.leaflet.web.processor.impl;

import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CustomSEODataProviderResponseProcessor}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CustomSEODataProviderResponseProcessorTest {

    private static final String SEO_TITLE = "SEO title";
    private static final String SEO_DESCRIPTION = "SEO Description";
    private static final String SEO_KEYWORDS = "SEO Keywords";

    private HttpServletRequest httpServletRequest;
    private CustomSEODataProviderResponseProcessor responseProcessor;

    @BeforeEach
    public void setup() {
        httpServletRequest = new MockHttpServletRequest();
        responseProcessor = new CustomSEODataProviderResponseProcessor(httpServletRequest);
    }

    @Test
    public void shouldProcessResponse() {

        // given
        EntryVO entryVO = EntryVO.getBuilder()
                .withSeoTitle(SEO_TITLE)
                .withSeoDescription(SEO_DESCRIPTION)
                .withSeoKeywords(SEO_KEYWORDS)
                .build();

        // when
        responseProcessor.process(entryVO);

        // then
        assertThat(httpServletRequest.getAttribute(RequestParameter.SEO_META_TITLE), equalTo(SEO_TITLE));
        assertThat(httpServletRequest.getAttribute(RequestParameter.SEO_META_DESCRIPTION), equalTo(SEO_DESCRIPTION));
        assertThat(httpServletRequest.getAttribute(RequestParameter.SEO_META_KEYWORDS), equalTo(SEO_KEYWORDS));
    }
}