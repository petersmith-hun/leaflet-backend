package hu.psprog.leaflet.web.validation;

import hu.psprog.leaflet.api.rest.request.common.AuthenticatedRequestModel;
import hu.psprog.leaflet.security.jwt.model.Role;
import hu.psprog.leaflet.web.test.WithMockedJWTUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AuthenticatedRequestValidator}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(SpringExtension.class),
        @ExtendWith(MockitoExtension.class)
})
@TestExecutionListeners(listeners = {
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class AuthenticatedRequestValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private AuthenticatedRequestValidator authenticatedRequestValidator;

    @ParameterizedTest
    @WithMockedJWTUser(userID = 1L, role = Role.ADMIN)
    @MethodSource("authenticationRequestValidatorDataProvider")
    public void shouldValidate(Long userID, boolean expectedValidationResult) {

        // given
        AuthenticatedRequestModel requestModel = prepareAuthenticatedRequestModel(userID);

        // when
        boolean result = authenticatedRequestValidator.isValid(requestModel, constraintValidatorContext);

        // then
        assertThat(result, is(expectedValidationResult));
    }

    @Test
    @WithMockUser
    public void shouldBeInvalidIfNonJWTAuthenticated() {

        // given
        AuthenticatedRequestModel requestModel = prepareAuthenticatedRequestModel(1L);

        // when
        boolean result = authenticatedRequestValidator.isValid(requestModel, constraintValidatorContext);

        // then
        assertThat(result, is(false));
    }

    private AuthenticatedRequestModel prepareAuthenticatedRequestModel(Long userID) {
        return () -> userID;
    }

    private static Stream<Arguments> authenticationRequestValidatorDataProvider() {

        return Stream.of(
                Arguments.of(1L, true),
                Arguments.of(null, true),
                Arguments.of(2L, false)
        );
    }
}