package hu.psprog.leaflet.service.impl.search.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
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
 * Unit tests for {@link CommentSearchHandler}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class CommentSearchHandlerTest {

    @Mock
    private SearchStrategy<CommentSearchParametersVO, Comment> searchStrategy1;

    @Mock
    private SearchStrategy<CommentSearchParametersVO, Comment> searchStrategy2;

    @Mock
    private SearchStrategy<CommentSearchParametersVO, Comment> searchStrategy3;

    @Mock
    private Root<Comment> root;

    @Mock
    private Path<Boolean> pathEnabled;

    @Mock
    private Path<Boolean> pathDeleted;

    @Mock
    private CriteriaQuery<Entry> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    private CommentSearchHandler commentSearchHandler;

    @BeforeEach
    public void setup() {
        commentSearchHandler = new CommentSearchHandler(List.of(searchStrategy1, searchStrategy2, searchStrategy3));
    }

    @Test
    public void shouldCreateSpecificationReturnSpecificationBuiltFromTheResponseOfApplicableStrategies() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = CommentSearchParametersVO.builder().build();

        given(searchStrategy1.isApplicable(commentSearchParametersVO)).willReturn(true);
        given(searchStrategy2.isApplicable(commentSearchParametersVO)).willReturn(false);
        given(searchStrategy3.isApplicable(commentSearchParametersVO)).willReturn(true);

        given(searchStrategy1.getSpecification(commentSearchParametersVO)).willReturn(CommentSpecification.IS_ENABLED);
        given(searchStrategy3.getSpecification(commentSearchParametersVO)).willReturn(CommentSpecification.IS_NOT_DELETED);

        // Mockito can't distinguish the SingularAttribute parameters, because they are null without an active JPA context
        given(root.get(nullable(SingularAttribute.class)))
                .willReturn(pathEnabled)
                .willReturn(pathDeleted);

        // when
        Specification<Comment> result = commentSearchHandler.createSpecification(commentSearchParametersVO);

        // then
        result.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder).equal(pathEnabled, true);
        verify(criteriaBuilder).equal(pathDeleted, false);
        verifyNoMoreInteractions(criteriaBuilder);
    }
}
