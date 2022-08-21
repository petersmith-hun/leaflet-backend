package hu.psprog.leaflet.web.validation;

import hu.psprog.leaflet.api.rest.request.common.AuthenticatedRequestModel;
import hu.psprog.leaflet.web.annotation.AuthenticatedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Objects;

/**
 * Validates authenticated request model.
 * Given authenticated user ID should be same as the one stored in the security context.
 *
 * @author Peter Smith
 */
public class AuthenticatedRequestValidator implements ConstraintValidator<AuthenticatedRequest, AuthenticatedRequestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedRequestValidator.class);

    private static final String USER_ID_TOKEN_ATTRIBUTE = "uid";

    @Override
    public void initialize(AuthenticatedRequest constraintAnnotation) {
        // do nothing
    }

    @Override
    public boolean isValid(AuthenticatedRequestModel value, ConstraintValidatorContext context) {

        boolean valid = false;
        if (Objects.isNull(value.getAuthenticatedUserId())) {
            valid = true;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken) {
                Map<String, Object> payload = ((JwtAuthenticationToken) authentication).getTokenAttributes();
                Long userID = (Long) payload.getOrDefault(USER_ID_TOKEN_ATTRIBUTE, 0);
                valid = userID.equals(value.getAuthenticatedUserId());

                if (!valid) {
                    LOGGER.warn("Request validation failed for userID={} - current userID={} is different", value.getAuthenticatedUserId(), userID);
                }
            }
        }

        return valid;
    }
}
