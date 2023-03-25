package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Entry_;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
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
 * Unit tests for {@link EntryByCategorySearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntryByCategorySearchStrategyTest {

    @Mock
    private Root<Entry> root;

    @Mock
    private Path<Category> path;

    @Mock
    private CriteriaQuery<Entry> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private EntryByCategorySearchStrategy strategy;

    @Test
    public void shouldGetSpecificationCreateSpecificationForGivenCategoryID() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(3L);

        given(root.get(Entry_.category)).willReturn(path);

        // when
        Specification<Entry> result = strategy.getSpecification(entrySearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(path, Category.getBuilder().withId(3L).build());
    }

    @Test
    public void shouldIsApplicableReturnTrueWhenCategoryIDParameterIsPresent() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(1L);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenCategoryIDParameterIsMissing() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private EntrySearchParametersVO prepareEntrySearchParametersVO(Long categoryID) {

        return EntrySearchParametersVO.builder()
                .categoryID(Optional.ofNullable(categoryID))
                .build();
    }
}
