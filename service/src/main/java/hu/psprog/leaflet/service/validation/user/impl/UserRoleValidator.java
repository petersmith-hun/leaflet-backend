package hu.psprog.leaflet.service.validation.user.impl;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.validation.Validator;
import hu.psprog.leaflet.service.validation.user.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Validates user's role.
 * User is considered invalid if its role is NO_LOGIN.
 *
 * @author Peter Smith
 */
@Component
public class UserRoleValidator implements UserValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleValidator.class);

    @Override
    public boolean isValid(User user) {

        boolean valid = user.getRole() != Role.NO_LOGIN;
        if (!valid) {
            LOGGER.warn("User '{}' with NO_LOGIN account tried to login.", user.getEmail());
        }

        return valid;
    }

    @Override
    public Validator<User> nextValidator() {
        return null;
    }
}
