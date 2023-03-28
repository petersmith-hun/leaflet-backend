package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.request.comment.CommentSearchParameters;
import hu.psprog.leaflet.api.rest.request.common.OrderBy;
import hu.psprog.leaflet.api.rest.request.common.OrderDirection;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link CommentSearchParameters} object to a {@link CommentSearchParametersVO} object.
 * Sets the following default pagination parameters if overrides are not present:
 *  - A page size of 10;
 *  - And ascending sorting by creation date.
 *
 * @author Peter Smith
 */
@Component
public class CommentSearchParametersConverter implements Converter<CommentSearchParameters, CommentSearchParametersVO> {

    private static final int DEFAULT_LIMIT = 10;
    private static final CommentVO.OrderBy DEFAULT_ORDER_BY = CommentVO.OrderBy.CREATED;
    private static final hu.psprog.leaflet.service.common.OrderDirection DEFAULT_ORDER_DIRECTION = hu.psprog.leaflet.service.common.OrderDirection.ASC;

    @Override
    public CommentSearchParametersVO convert(CommentSearchParameters source) {
        
        return CommentSearchParametersVO.builder()
                .enabled(source.getEnabled())
                .deleted(source.getDeleted())
                .content(source.getContent())
                .page(source.getPage())
                .limit(mapLimit(source))
                .orderBy(mapOrderBy(source))
                .orderDirection(mapOrderDirection(source))
                .build();
    }

    private Integer mapLimit(CommentSearchParameters source) {

        return source.getLimit()
                .orElse(DEFAULT_LIMIT);
    }

    private CommentVO.OrderBy mapOrderBy(CommentSearchParameters source) {

        return source.getOrderBy()
                .map(OrderBy.Comment::name)
                .map(CommentVO.OrderBy::valueOf)
                .orElse(DEFAULT_ORDER_BY);
    }

    private hu.psprog.leaflet.service.common.OrderDirection mapOrderDirection(CommentSearchParameters source) {

        return source.getOrderDirection()
                .map(OrderDirection::name)
                .map(hu.psprog.leaflet.service.common.OrderDirection::valueOf)
                .orElse(DEFAULT_ORDER_DIRECTION);
    }
}
