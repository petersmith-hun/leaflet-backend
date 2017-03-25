package hu.psprog.leaflet.web.annotation;

import hu.psprog.leaflet.web.validation.AuthenticatedRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates to check authenticated user ID in given model.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = AuthenticatedRequestValidator.class)
public @interface AuthenticatedRequest {

    String message() default "{validation.request.authenticated.check}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
