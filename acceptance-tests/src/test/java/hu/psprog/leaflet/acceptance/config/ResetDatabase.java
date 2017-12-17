package hu.psprog.leaflet.acceptance.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark test methods which change the database content,
 * so the database should be reset after test run.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResetDatabase {
}
