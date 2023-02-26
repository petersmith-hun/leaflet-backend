package hu.psprog.leaflet.service.vo;

import lombok.Builder;
import lombok.Data;

/**
 * Tag assignment VO.
 *
 * @author Peter Smith
 */
@Data
@Builder(builderMethodName = "getBuilder", setterPrefix = "with")
public class TagAssignmentVO {

    private final Long entryID;
    private final Long tagID;
}
