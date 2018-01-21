package hu.psprog.leaflet.web.validation;

import hu.psprog.leaflet.api.rest.request.common.AuthenticatedRequestModel;
import hu.psprog.leaflet.security.jwt.model.Role;
import hu.psprog.leaflet.web.test.WithMockedJWTUser;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import javax.validation.ConstraintValidatorContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AuthenticatedRequestValidator}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@TestExecutionListeners(listeners = {
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class AuthenticatedRequestValidatorTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private AuthenticatedRequestValidator authenticatedRequestValidator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockedJWTUser(userID = 1L, role = Role.ADMIN)
    @Parameters(source = AuthenticatedRequestValidatorParameterProvider.class)
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

    public static class AuthenticatedRequestValidatorParameterProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {1L, true},
                    new Object[] {null, true},
                    new Object[] {2L, false}
            };
        }
    }
}