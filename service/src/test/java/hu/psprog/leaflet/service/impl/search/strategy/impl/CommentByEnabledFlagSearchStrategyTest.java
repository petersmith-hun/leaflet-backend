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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CommentByEnabledFlagSearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class CommentByEnabledFlagSearchStrategyTest {

    @Mock
    private Root<Comment> root;

    @Mock
    private Path<Boolean> path;

    @Mock
    private CriteriaQuery<Comment> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private CommentByEnabledFlagSearchStrategy strategy;

    @Test
    public void shouldGetSpecificationReturnIsEnabledSpecificationForTrueEnabledFlagInRequest() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(true);

        given(root.get(Comment_.enabled)).willReturn(path);

        // when
        Specification<Comment> result = strategy.getSpecification(commentSearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(path, true);
    }

    @Test
    public void shouldGetSpecificationReturnIsDisabledSpecificationForFalseEnabledFlagInRequest() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(false);

        given(root.get(Comment_.enabled)).willReturn(path);

        // when
        Specification<Comment> result = strategy.getSpecification(commentSearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(path, false);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void shouldIsApplicableReturnTrueWhenEnabledParameterIsPresent(boolean enabled) {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(enabled);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenEntryStatusParameterIsMissing() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private CommentSearchParametersVO prepareCommentSearchParametersVO(Boolean enabledFlag) {

        return CommentSearchParametersVO.builder()
                .enabled(Optional.ofNullable(enabledFlag))
                .build();
    }
}
