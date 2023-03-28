package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.service.common.OrderDirection;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

/**
 * Internal domain class representing an entry search request.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class CommentSearchParametersVO {

    private Optional<Boolean> enabled;
    private Optional<Boolean> deleted;
    private Optional<String> content;

    private CommentVO.OrderBy orderBy;
    private OrderDirection orderDirection;
    private Integer limit;
    private Integer page;

    public static class CommentSearchParametersVOBuilder {

        private Optional<Boolean> enabled = Optional.empty();
        private Optional<Boolean> deleted = Optional.empty();
        private Optional<String> content = Optional.empty();
    }
}
