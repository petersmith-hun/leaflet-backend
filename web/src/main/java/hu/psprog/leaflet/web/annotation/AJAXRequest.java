package hu.psprog.leaflet.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requests marked as AJAX type requests will not wrap their response into {@link hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel} model.
 *
 * @author Peter Smith
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AJAXRequest {
}
