package hu.psprog.leaflet.service.validation.user.impl;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.validation.Validator;
import hu.psprog.leaflet.service.validation.user.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Null-validation for user objects.
 * User object is considered valid if it is not null.
 *
 * @author Peter Smith
 */
@Component
public class UserNullValidator implements UserValidator {

    @Autowired
    private UserRoleValidator userRoleValidator;

    @Override
    public boolean isValid(User user) {
        return Objects.nonNull(user);
    }

    @Override
    public Validator<User> nextValidator() {
        return userRoleValidator;
    }
}
