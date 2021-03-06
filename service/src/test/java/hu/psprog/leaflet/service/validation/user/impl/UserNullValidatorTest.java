package hu.psprog.leaflet.service.validation.user.impl;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.validation.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UserNullValidator}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserNullValidatorTest {

    @Mock
    private UserRoleValidator userRoleValidator;

    @InjectMocks
    private UserNullValidator userNullValidator;

    @Test
    public void shouldIsValidReturnTrueWithNonNullUser() {

        // when
        boolean result = userNullValidator.isValid(new User());

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsValidReturnFalseWithNullUser() {

        // when
        boolean result = userNullValidator.isValid(null);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnNextValidator() {

        // when
        Validator<User> result = userNullValidator.nextValidator();

        // then
        assertThat(result, equalTo(userRoleValidator));
    }
}