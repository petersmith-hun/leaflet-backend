package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Comment_;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CommentContentSearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class CommentContentSearchStrategyTest {

    @Mock
    private Root<Comment> root;

    @Mock
    private Path<String> pathContent;

    @Mock
    private Predicate pathPredicateContent;

    @Mock
    private CriteriaQuery<Comment> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private CommentContentSearchStrategy strategy;

    @Test
    public void shouldGetSpecificationCreateSpecificationForGivenContent() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO("some content");
        String expectedLikeExpression = "%some%content%";

        given(criteriaBuilder.like(pathContent, expectedLikeExpression)).willReturn(pathPredicateContent);
        given(root.get(Comment_.content)).willReturn(pathContent);

        // when
        Specification<Comment> result = strategy.getSpecification(commentSearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).like(pathContent, expectedLikeExpression);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abd", "abcd", "content"})
    public void shouldIsApplicableReturnTrueWhenContentParameterIsPresentAndLongerThanOrEqualTo3Chars(String content) {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(content);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "a", ""})
    public void shouldIsApplicableReturnFalseWhenContentParameterIsPresentButShorterThan3Chars(String content) {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(content);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenContentParameterIsMissing() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private CommentSearchParametersVO prepareCommentSearchParametersVO(String content) {

        return CommentSearchParametersVO.builder()
                .content(Optional.ofNullable(content))
                .build();
    }
}