package hu.psprog.leaflet.web.validation;

import hu.psprog.leaflet.api.rest.request.common.AuthenticatedRequestModel;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.JWTPayload;
import hu.psprog.leaflet.web.annotation.AuthenticatedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates authenticated request model.
 * Given authenticated user ID should be same as the one stored in the security context.
 *
 * @author Peter Smith
 */
public class AuthenticatedRequestValidator implements ConstraintValidator<AuthenticatedRequest, AuthenticatedRequestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedRequestValidator.class);

    @Override
    public void initialize(AuthenticatedRequest constraintAnnotation) {
        // do nothing
    }

    @Override
    public boolean isValid(AuthenticatedRequestModel value, ConstraintValidatorContext context) {

        boolean valid = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JWTAuthenticationToken) {
            JWTPayload payload = (JWTPayload) authentication.getDetails();
            valid = payload.getId() == value.getAuthenticatedUserId().intValue();

            if (!valid) {
                LOGGER.warn("Request validation failed for userID={} - current userID={} is different", value.getAuthenticatedUserId(), payload.getId());
            }
        }

        return valid;
    }
}
