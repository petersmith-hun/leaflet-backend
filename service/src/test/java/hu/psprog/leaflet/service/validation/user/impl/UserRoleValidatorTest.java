package hu.psprog.leaflet.service.validation.user.impl;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.validation.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UserRoleValidator}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRoleValidatorTest {

    @InjectMocks
    private UserRoleValidator userRoleValidator;

    @Test
    public void shouldReturnTrueWithOtherThanNoLoginRole() {

        // when
        boolean result = userRoleValidator.isValid(prepareUser(Role.ADMIN));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWithNoLoginRole() {

        // when
        boolean result = userRoleValidator.isValid(prepareUser(Role.NO_LOGIN));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnNextValidator() {

        // when
        Validator<User> result = userRoleValidator.nextValidator();

        // then
        assertThat(result, nullValue());
    }

    private User prepareUser(Role role) {
        return User.getBuilder()
                .withRole(role)
                .build();
    }
}