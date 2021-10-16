package hu.psprog.leaflet.service.validation.user;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.validation.Validator;
import hu.psprog.leaflet.service.validation.user.impl.UserNullValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link UserValidatorChain}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserValidatorChainTest {

    private static final User USER = new User();

    @Mock(lenient = true)
    private UserNullValidator mockedTrueValidator;

    @Mock(lenient = true)
    private Validator<User> mockedFalseValidator;

    private UserValidatorChain userValidatorChain;

    @BeforeEach
    public void setup() {
        given(mockedTrueValidator.isValid(any(User.class))).willReturn(true);
        given(mockedFalseValidator.isValid(any(User.class))).willReturn(false);
    }

    @Test
    public void shouldReturnFalseOnEmptyChain() {

        // given
        userValidatorChain = new UserValidatorChain(null);

        // when
        boolean result = userValidatorChain.runChain(USER);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnTrueWithNoFalseValidator() {

        // given
        userValidatorChain = new UserValidatorChain(mockedTrueValidator);

        // when
        boolean result = userValidatorChain.runChain(USER);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWithFalseValidator() {

        // given
        userValidatorChain = new UserValidatorChain(mockedTrueValidator);
        given(mockedTrueValidator.nextValidator()).willReturn(mockedFalseValidator);

        // when
        boolean result = userValidatorChain.runChain(USER);

        // then
        assertThat(result, is(false));
    }
}