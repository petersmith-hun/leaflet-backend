package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.EntryStatus;
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
public class EntrySearchParametersVO {

    private Optional<Long> categoryID;
    private Optional<Long> tagID;
    private Optional<Boolean> enabled;
    private Optional<EntryStatus> status;
    private Optional<String> content;

    private EntryVO.OrderBy orderBy;
    private OrderDirection orderDirection;
    private Integer limit;
    private Integer page;

    public static class EntrySearchParametersVOBuilder {

        private Optional<Long> categoryID = Optional.empty();
        private Optional<Long> tagID = Optional.empty();
        private Optional<Boolean> enabled = Optional.empty();
        private Optional<EntryStatus> status = Optional.empty();
        private Optional<String> content = Optional.empty();
    }
}
