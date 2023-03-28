package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Comment} entities to generate a {@link Specification} that checks if
 * a comment contains a specific expression.
 * Only applicable if the {@link CommentSearchParametersVO} object contains the expected expression, and it is at least
 * 3 characters long (inclusive).
 *
 * @author Peter Smith
 */
@Component
public class CommentContentSearchStrategy implements SearchStrategy<CommentSearchParametersVO, Comment> {

    private static final int MINIMUM_CONTENT_LENGTH = 3;

    @Override
    public Specification<Comment> getSpecification(CommentSearchParametersVO searchParameters) {
        return CommentSpecification.containsExpression(searchParameters.getContent().get());
    }

    @Override
    public boolean isApplicable(CommentSearchParametersVO searchParameters) {

        return searchParameters.getContent()
                .filter(content -> content.length() >= MINIMUM_CONTENT_LENGTH)
                .isPresent();
    }
}
