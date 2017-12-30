package hu.psprog.leaflet.acceptance.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a test that it creates a file which is needed to be removed after test execution.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ClearStorage {

    /**
     * Storage-root-relative file path.
     *
     * @return file path
     */
    String path();
}
