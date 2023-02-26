package hu.psprog.leaflet.service.vo;

import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Parent for all VOs.
 *
 * @author Peter Smith
 */
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class BaseVO implements Serializable {

}
