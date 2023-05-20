package hu.psprog.leaflet.service.impl.search.impl;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit tests for {@link EntrySearchHandler}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntrySearchHandlerTest {

    @Mock
    private SearchStrategy<EntrySearchParametersVO, Entry> searchStrategy1;

    @Mock
    private SearchStrategy<EntrySearchParametersVO, Entry> searchStrategy2;

    @Mock
    private SearchStrategy<EntrySearchParametersVO, Entry> searchStrategy3;

    @Mock
    private Root<Entry> root;

    @Mock
    private Path<Boolean> pathEnabled;

    @Mock
    private Path<EntryStatus> pathStatus;

    @Mock
    private CriteriaQuery<Entry> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    private EntrySearchHandler entrySearchHandler;

    @BeforeEach
    public void setup() {
        entrySearchHandler = new EntrySearchHandler(List.of(searchStrategy1, searchStrategy2, searchStrategy3));
    }

    @Test
    public void shouldCreateSpecificationReturnSpecificationBuiltFromTheResponseOfApplicableStrategies() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = EntrySearchParametersVO.builder().build();

        given(searchStrategy1.isApplicable(entrySearchParametersVO)).willReturn(true);
        given(searchStrategy2.isApplicable(entrySearchParametersVO)).willReturn(false);
        given(searchStrategy3.isApplicable(entrySearchParametersVO)).willReturn(true);

        given(searchStrategy1.getSpecification(entrySearchParametersVO)).willReturn(EntrySpecification.IS_ENABLED);
        given(searchStrategy3.getSpecification(entrySearchParametersVO)).willReturn(EntrySpecification.IS_PUBLIC);

        // Mockito can't distinguish the SingularAttribute parameters, because they are null without an active JPA context
        given(root.get(nullable(SingularAttribute.class)))
                .willReturn(pathEnabled)
                .willReturn(pathStatus);

        // when
        Specification<Entry> result = entrySearchHandler.createSpecification(entrySearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(pathEnabled, true);
        verify(criteriaBuilder).equal(pathStatus, EntryStatus.PUBLIC);
        verifyNoMoreInteractions(criteriaBuilder);
    }
}
