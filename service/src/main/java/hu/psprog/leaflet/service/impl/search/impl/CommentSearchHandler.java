package hu.psprog.leaflet.service.impl.search.impl;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.service.impl.search.strategy.SearchStrategy;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

/**
 * {@link AbstractSearchHandler} implementation for comment search requests.
 *
 * @author Peter Smith
 */
@Component
public class CommentSearchHandler extends AbstractSearchHandler<CommentSearchParametersVO, Comment> {

    @Autowired
    public CommentSearchHandler(List<SearchStrategy<CommentSearchParametersVO, Comment>> searchStrategies) {
        super(searchStrategies);
    }

    @Override
    protected Supplier<Specification<Comment>> baseSpecificationSupplier() {
        return () -> Specification.where(null);
    }
}
