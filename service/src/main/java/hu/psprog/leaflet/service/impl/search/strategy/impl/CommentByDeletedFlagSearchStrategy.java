package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Comment} entities to generate a {@link Specification} that checks if
 * a comment is logically deleted or not.
 * Only applicable if the {@link CommentSearchParametersVO} object contains the deleted flag.
 *
 * @author Peter Smith
 */
@Component
public class CommentByDeletedFlagSearchStrategy implements SearchStrategy<CommentSearchParametersVO, Comment> {

    @Override
    public Specification<Comment> getSpecification(CommentSearchParametersVO searchParameters) {

        return searchParameters.getDeleted().get()
                ? CommentSpecification.IS_DELETED
                : CommentSpecification.IS_NOT_DELETED;
    }

    @Override
    public boolean isApplicable(CommentSearchParametersVO searchParameters) {
        return searchParameters.getDeleted().isPresent();
    }
}
