package hu.psprog.leaflet.service.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * VO for contact request processing.
 *
 * @author Peter Smith
 */
@Data
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class ContactRequestVO {

    private final String name;
    private final String email;
    private final String message;
}
