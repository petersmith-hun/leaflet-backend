package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Comment_;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Unit tests for {@link CommentByUserSearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class CommentByUserSearchStrategyTest {

    @Mock
    private Root<Comment> root;

    @Mock
    private Path<User> path;

    @Mock
    private CriteriaQuery<Comment> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private CommentByUserSearchStrategy strategy;

    @Test
    public void shouldGetSpecificationCreateSpecificationForGivenUserID() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(8L);

        given(root.get(Comment_.user)).willReturn(path);

        // when
        Specification<Comment> result = strategy.getSpecification(commentSearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(path, User.getBuilder().withId(8L).build());
    }

    @Test
    public void shouldIsApplicableReturnTrueWhenUserIDParameterIsPresent() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(1L);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenUserIDParameterIsMissing() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private CommentSearchParametersVO prepareCommentSearchParametersVO(Long userID) {

        return CommentSearchParametersVO.builder()
                .userID(Optional.ofNullable(userID))
                .build();
    }
}
