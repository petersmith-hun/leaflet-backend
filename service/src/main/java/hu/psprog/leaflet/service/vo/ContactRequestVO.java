package hu.psprog.leaflet.service.vo;

import lombok.Builder;
import lombok.Data;

/**
 * VO for contact request processing.
 *
 * @author Peter Smith
 */
@Data
@Builder(builderMethodName = "getBuilder", setterPrefix = "with")
public class ContactRequestVO {

    private final String name;
    private final String email;
    private final String message;
}
