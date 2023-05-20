package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
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
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryContentSearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntryContentSearchStrategyTest {

    @Mock
    private Root<Entry> root;

    @Mock
    private Path<String> pathTitle;

    @Mock
    private Path<String> pathPrologue;

    @Mock
    private Path<String> pathRawContent;

    @Mock
    private Predicate pathPredicateTitle;

    @Mock
    private Predicate pathPredicatePrologue;

    @Mock
    private Predicate pathPredicateRawContent;

    @Mock
    private CriteriaQuery<Entry> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private EntryContentSearchStrategy strategy;

    @Test
    public void shouldGetSpecificationCreateSpecificationForGivenContent() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO("some content");
        String expectedLikeExpression = "%some%content%";

        given(criteriaBuilder.like(pathTitle, expectedLikeExpression)).willReturn(pathPredicateTitle);
        given(criteriaBuilder.like(pathPrologue, expectedLikeExpression)).willReturn(pathPredicatePrologue);
        given(criteriaBuilder.like(pathRawContent, expectedLikeExpression)).willReturn(pathPredicateRawContent);

        // Mockito can't distinguish the SingularAttribute parameters, because they are null without an active JPA context
        given(root.get(nullable(SingularAttribute.class)))
                .willReturn(pathTitle)
                .willReturn(pathPrologue)
                .willReturn(pathRawContent);

        // when
        Specification<Entry> result = strategy.getSpecification(entrySearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).like(pathTitle, expectedLikeExpression);
        verify(criteriaBuilder).like(pathPrologue, expectedLikeExpression);
        verify(criteriaBuilder).like(pathRawContent, expectedLikeExpression);
        verify(criteriaBuilder).or(pathPredicateTitle, pathPredicatePrologue, pathPredicateRawContent);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abd", "abcd", "content"})
    public void shouldIsApplicableReturnTrueWhenContentParameterIsPresentAndLongerThanOrEqualTo3Chars(String content) {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(content);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "a", ""})
    public void shouldIsApplicableReturnFalseWhenContentParameterIsPresentButShorterThan3Chars(String content) {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(content);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenContentParameterIsMissing() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private EntrySearchParametersVO prepareEntrySearchParametersVO(String content) {

        return EntrySearchParametersVO.builder()
                .content(Optional.ofNullable(content))
                .build();
    }
}
