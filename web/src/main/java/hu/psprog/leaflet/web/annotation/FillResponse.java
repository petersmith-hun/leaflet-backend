package hu.psprog.leaflet.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to command the response filler aspect to process response.
 *
 * @author Peter Smith
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FillResponse {

    /**
     * Fill mode attribute.
     *
     * @return returns current fill mode,  {@code ResponseFillMode.ALL} by default
     */
    ResponseFillMode fill() default ResponseFillMode.ALL;
}
