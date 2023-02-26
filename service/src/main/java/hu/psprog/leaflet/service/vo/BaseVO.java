package hu.psprog.leaflet.service.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Parent for all VOs.
 *
 * @author Peter Smith
 */
@Data
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class BaseVO implements Serializable {

}
