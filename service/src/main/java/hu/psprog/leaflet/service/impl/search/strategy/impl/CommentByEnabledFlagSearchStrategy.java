package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Comment} entities to generate a {@link Specification} that checks if
 * a comment is enabled or disabled.
 * Only applicable if the {@link CommentSearchParametersVO} object contains the enabled flag.
 *
 * @author Peter Smith
 */
@Component
public class CommentByEnabledFlagSearchStrategy implements SearchStrategy<CommentSearchParametersVO, Comment> {

    @Override
    public Specification<Comment> getSpecification(CommentSearchParametersVO searchParameters) {

        return searchParameters.getEnabled().get()
                ? CommentSpecification.IS_ENABLED
                : CommentSpecification.IS_DISABLED;
    }

    @Override
    public boolean isApplicable(CommentSearchParametersVO searchParameters) {
        return searchParameters.getEnabled().isPresent();
    }
}
