package hu.psprog.leaflet.service.validation.user;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.validation.AbstractValidatorChain;
import hu.psprog.leaflet.service.validation.user.impl.UserNullValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Returns user validator chain.
 *
 * @author Peter Smith
 */
@Component
public class UserValidatorChain extends AbstractValidatorChain<UserValidator, User> {

    @Autowired
    public UserValidatorChain(final UserNullValidator userNullValidator) {
        super(userNullValidator);
    }
}
