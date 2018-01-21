package hu.psprog.leaflet.web.rest.filler.impl;

import hu.psprog.leaflet.api.rest.response.common.PaginationDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.web.rest.filler.RequestParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link PaginationResponseFiller}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class PaginationResponseFillerTest {

    private static final long ENTITY_COUNT = 30L;
    private static final int ENTITY_COUNT_ON_PAGE = 10;
    private static final int PAGE_COUNT = 3;
    private static final int PAGE_NUMBER = 2;
    private static final boolean FIRST = false;
    private static final boolean LAST = false;
    private static final boolean HAS_NEXT = true;
    private static final boolean HAS_PREVIOUS = true;

    private PaginationResponseFiller responseFiller;

    @Test
    public void shouldFillResponse() {

        // given
        prepareFillerMock(true);
        WrapperBodyDataModel.WrapperBodyDataModelBuilder builder = WrapperBodyDataModel.getBuilder();

        // when
        responseFiller.fill(builder);

        // then
        assertThat(builder.build().getPagination(), equalTo(preparePaginationDataModel()));
    }

    @Test
    public void shouldFill() {

        // given
        prepareFillerMock(true);

        // when
        boolean result = responseFiller.shouldFill();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldNotFill() {

        // given
        prepareFillerMock(false);

        // when
        boolean result = responseFiller.shouldFill();

        // then
        assertThat(result, is(false));
    }

    private PaginationDataModel preparePaginationDataModel() {
        return PaginationDataModel.getBuilder()
                .withEntityCount(ENTITY_COUNT)
                .withEntityCountOnPage(ENTITY_COUNT_ON_PAGE)
                .withPageCount(PAGE_COUNT)
                .withPageNumber(PAGE_NUMBER)
                .withFirst(FIRST)
                .withLast(LAST)
                .withHasNext(HAS_NEXT)
                .withHasPrevious(HAS_PREVIOUS)
                .build();
    }

    private void prepareFillerMock(boolean populateRequest) {

        HttpServletRequest request = new MockHttpServletRequest();
        if (populateRequest) {
            request.setAttribute(RequestParameter.PAGINATION_ENTITY_COUNT, ENTITY_COUNT);
            request.setAttribute(RequestParameter.PAGINATION_ENTITY_COUNT_ON_PAGE, ENTITY_COUNT_ON_PAGE);
            request.setAttribute(RequestParameter.PAGINATION_PAGE_COUNT, PAGE_COUNT);
            request.setAttribute(RequestParameter.PAGINATION_PAGE_NUMBER, PAGE_NUMBER);
            request.setAttribute(RequestParameter.PAGINATION_IS_FIRST, FIRST);
            request.setAttribute(RequestParameter.PAGINATION_IS_LAST, LAST);
            request.setAttribute(RequestParameter.PAGINATION_HAS_NEXT, HAS_NEXT);
            request.setAttribute(RequestParameter.PAGINATION_HAS_PREVIOUS, HAS_PREVIOUS);
        }

        responseFiller = new PaginationResponseFiller(request);
    }
}