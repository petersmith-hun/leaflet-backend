package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Entry_;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
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
 * Unit tests for {@link EntryByEnabledFlagSearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntryByEnabledFlagSearchStrategyTest {

    @Mock
    private Root<Entry> root;

    @Mock
    private Path<Boolean> path;

    @Mock
    private CriteriaQuery<Entry> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private EntryByEnabledFlagSearchStrategy strategy;

    @Test
    public void shouldGetSpecificationReturnIsEnabledSpecificationForTrueEnabledFlagInRequest() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(true);

        given(root.get(Entry_.enabled)).willReturn(path);

        // when
        Specification<Entry> result = strategy.getSpecification(entrySearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(path, true);
    }

    @Test
    public void shouldGetSpecificationReturnIsDisabledSpecificationForFalseEnabledFlagInRequest() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(false);

        given(root.get(Entry_.enabled)).willReturn(path);

        // when
        Specification<Entry> result = strategy.getSpecification(entrySearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(path, false);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void shouldIsApplicableReturnTrueWhenEnabledParameterIsPresent(boolean enabled) {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(enabled);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenEntryStatusParameterIsMissing() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private EntrySearchParametersVO prepareEntrySearchParametersVO(Boolean enabledFlag) {

        return EntrySearchParametersVO.builder()
                .enabled(Optional.ofNullable(enabledFlag))
                .build();
    }
}
