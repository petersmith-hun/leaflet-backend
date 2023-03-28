package hu.psprog.leaflet.service.impl.search.strategy.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.repository.specification.CommentSpecification;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * {@link SearchStrategy} implementation for {@link Comment} entities to generate a {@link Specification} that checks if
 * a comment is created under the given entry.
 * Only applicable if the {@link CommentSearchParametersVO} object contains an entry ID.
 *
 * @author Peter Smith
 */
@Component
public class CommentByEntrySearchStrategy implements SearchStrategy<CommentSearchParametersVO, Comment> {

    @Override
    public Specification<Comment> getSpecification(CommentSearchParametersVO searchParameters) {

        var entry = new Entry();
        searchParameters.getEntryID().ifPresent(entry::setId);

        return CommentSpecification.isOwnedByEntry(entry);
    }

    @Override
    public boolean isApplicable(CommentSearchParametersVO searchParameters) {
        return searchParameters.getEntryID().isPresent();
    }
}
