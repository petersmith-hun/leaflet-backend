package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.Entry_;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
 * Unit tests for {@link EntryPublicationStatusSearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntryPublicationStatusSearchStrategyTest {

    @Mock
    private Root<Entry> root;

    @Mock
    private Path<EntryStatus> path;

    @Mock
    private CriteriaQuery<Entry> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private EntryPublicationStatusSearchStrategy strategy;

    @ParameterizedTest
    @EnumSource(EntryStatus.class)
    public void shouldGetSpecificationCreateSpecificationForGivenStatus(EntryStatus entryStatus) {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(entryStatus);

        given(root.get(Entry_.status)).willReturn(path);

        // when
        Specification<Entry> result = strategy.getSpecification(entrySearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);
        verify(criteriaBuilder).equal(path, entryStatus);
    }

    @ParameterizedTest
    @EnumSource(EntryStatus.class)
    public void shouldIsApplicableReturnTrueWhenEntryStatusParameterIsPresent(EntryStatus entryStatus) {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(entryStatus);

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

    private EntrySearchParametersVO prepareEntrySearchParametersVO(EntryStatus entryStatus) {

        return EntrySearchParametersVO.builder()
                .status(Optional.ofNullable(entryStatus))
                .build();
    }
}
