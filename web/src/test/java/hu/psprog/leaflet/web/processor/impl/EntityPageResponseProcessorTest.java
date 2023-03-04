package hu.psprog.leaflet.web.processor.impl;

import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EntityPageResponseProcessor}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntityPageResponseProcessorTest {

    private static final long ENTITY_COUNT = 30L;
    private static final int ENTITY_COUNT_ON_PAGE = 10;
    private static final int PAGE_COUNT = 3;
    private static final int PAGE_NUMBER = 2;
    private static final boolean FIRST = false;
    private static final boolean LAST = false;
    private static final boolean HAS_NEXT = true;
    private static final boolean HAS_PREVIOUS = true;

    private HttpServletRequest httpServletRequest;
    private EntityPageResponseProcessor responseProcessor;

    @BeforeEach
    public void setup() {
        httpServletRequest = new MockHttpServletRequest();
        responseProcessor = new EntityPageResponseProcessor(httpServletRequest);
    }

    @Test
    public void shouldProcessResponse() {

        // given
        EntityPageVO<EntryVO> entityPageVO = EntityPageVO.<EntryVO>getBuilder()
                .withEntityCount(ENTITY_COUNT)
                .withEntityCountOnPage(ENTITY_COUNT_ON_PAGE)
                .withPageCount(PAGE_COUNT)
                .withPageNumber(PAGE_NUMBER)
                .withFirst(FIRST)
                .withLast(LAST)
                .withHasNext(HAS_NEXT)
                .withHasPrevious(HAS_PREVIOUS)
                .build();

        // when
        responseProcessor.process(entityPageVO);

        // then
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_ENTITY_COUNT), equalTo(ENTITY_COUNT));
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_ENTITY_COUNT_ON_PAGE), equalTo(ENTITY_COUNT_ON_PAGE));
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_PAGE_COUNT), equalTo(PAGE_COUNT));
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_PAGE_NUMBER), equalTo(PAGE_NUMBER));
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_IS_FIRST), equalTo(FIRST));
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_IS_LAST), equalTo(LAST));
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_HAS_NEXT), equalTo(HAS_NEXT));
        assertThat(httpServletRequest.getAttribute(RequestParameter.PAGINATION_HAS_PREVIOUS), equalTo(HAS_PREVIOUS));
    }
}