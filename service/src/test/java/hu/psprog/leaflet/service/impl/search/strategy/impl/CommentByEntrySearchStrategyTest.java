package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Comment_;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CommentByEntrySearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class CommentByEntrySearchStrategyTest {

    @Mock
    private Root<Comment> root;

    @Mock
    private Path<Entry> path;

    @Mock
    private CriteriaQuery<Comment> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private CommentByEntrySearchStrategy strategy;

    @Test
    public void shouldGetSpecificationCreateSpecificationForGivenEntryID() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(9L);

        given(root.get(Comment_.entry)).willReturn(path);

        // when
        Specification<Comment> result = strategy.getSpecification(commentSearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(path, Entry.getBuilder().withId(9L).build());
    }

    @Test
    public void shouldIsApplicableReturnTrueWhenEntryIDParameterIsPresent() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(1L);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenEntryIDParameterIsMissing() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = prepareCommentSearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(commentSearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private CommentSearchParametersVO prepareCommentSearchParametersVO(Long entryID) {

        return CommentSearchParametersVO.builder()
                .entryID(Optional.ofNullable(entryID))
                .build();
    }
}
