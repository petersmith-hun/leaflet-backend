package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.request.comment.CommentSearchParameters;
import hu.psprog.leaflet.api.rest.request.common.OrderBy;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CommentSearchParametersConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class CommentSearchParametersConverterTest {

    @InjectMocks
    private CommentSearchParametersConverter converter;

    @Test
    public void shouldConvertEmptyCommentSearchParametersObject() {

        // given
        CommentSearchParameters entrySearchParameters = new CommentSearchParameters();
        CommentSearchParametersVO expectedResult = CommentSearchParametersVO.builder()
                .enabled(Optional.empty())
                .content(Optional.empty())
                .page(1)
                .limit(10)
                .orderBy(CommentVO.OrderBy.CREATED)
                .orderDirection(OrderDirection.ASC)
                .build();

        // when
        CommentSearchParametersVO result = converter.convert(entrySearchParameters);

        // then
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void shouldConvertPopulatedCommentSearchParametersObject() {

        // given
        CommentSearchParameters commentSearchParameters = new CommentSearchParameters();
        commentSearchParameters.setEnabled(true);
        commentSearchParameters.setContent("content1");
        commentSearchParameters.setPage(2);
        commentSearchParameters.setLimit(30);
        commentSearchParameters.setOrderBy(OrderBy.Comment.ID);
        commentSearchParameters.setOrderDirection(hu.psprog.leaflet.api.rest.request.common.OrderDirection.DESC);

        CommentSearchParametersVO expectedResult = CommentSearchParametersVO.builder()
                .enabled(Optional.of(true))
                .content(Optional.of("content1"))
                .page(2)
                .limit(30)
                .orderBy(CommentVO.OrderBy.ID)
                .orderDirection(OrderDirection.DESC)
                .build();

        // when
        CommentSearchParametersVO result = converter.convert(commentSearchParameters);

        // then
        assertThat(result, equalTo(expectedResult));
    }
}
