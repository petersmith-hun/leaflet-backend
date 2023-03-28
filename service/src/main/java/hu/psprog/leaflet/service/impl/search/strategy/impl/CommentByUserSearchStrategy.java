package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Comment} entities to generate a {@link Specification} that checks if
 * a comment is created by the given user.
 * Only applicable if the {@link CommentSearchParametersVO} object contains a user ID.
 *
 * @author Peter Smith
 */
@Component
public class CommentByUserSearchStrategy implements SearchStrategy<CommentSearchParametersVO, Comment> {

    @Override
    public Specification<Comment> getSpecification(CommentSearchParametersVO searchParameters) {

        var user = new User();
        searchParameters.getUserID().ifPresent(user::setId);

        return CommentSpecification.isOwnedByUser(user);
    }

    @Override
    public boolean isApplicable(CommentSearchParametersVO searchParameters) {
        return searchParameters.getUserID().isPresent();
    }
}
