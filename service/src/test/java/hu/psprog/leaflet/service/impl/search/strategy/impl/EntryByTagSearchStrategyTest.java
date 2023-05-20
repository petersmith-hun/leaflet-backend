package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Entry_;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryByTagSearchStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntryByTagSearchStrategyTest {

    @Mock
    private Root<Entry> root;

    @Mock
    private Path<List<Tag>> path;

    @Mock
    private CriteriaQuery<Entry> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private EntryByTagSearchStrategy strategy;

    @Test
    public void shouldGetSpecificationCreateSpecificationForGivenTagID() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(5L);

        given(root.get(Entry_.tags)).willReturn(path);

        // when
        Specification<Entry> result = strategy.getSpecification(entrySearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).isMember(Tag.getBuilder().withId(5L).build(), path);
    }

    @Test
    public void shouldIsApplicableReturnTrueWhenTagIDParameterIsPresent() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(1L);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsApplicableReturnFalseWhenTagIDParameterIsMissing() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = prepareEntrySearchParametersVO(null);

        // when
        boolean result = strategy.isApplicable(entrySearchParametersVO);

        // then
        assertThat(result, is(false));
    }

    private EntrySearchParametersVO prepareEntrySearchParametersVO(Long tagID) {

        return EntrySearchParametersVO.builder()
                .tagID(Optional.ofNullable(tagID))
                .build();
    }
}
