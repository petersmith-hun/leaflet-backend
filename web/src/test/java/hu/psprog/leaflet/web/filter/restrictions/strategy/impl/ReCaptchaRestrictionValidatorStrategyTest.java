package hu.psprog.leaflet.web.filter.restrictions.strategy.impl;

import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionRoute;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.exception.ClientSecurityViolationException;
import hu.psprog.leaflet.web.filter.restrictions.strategy.impl.recaptcha.service.ReCaptchaValidationService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;

import static hu.psprog.leaflet.web.filter.ClientAcceptorFilter.HEADER_CLIENT_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link ReCaptchaRestrictionValidatorStrategy}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class ReCaptchaRestrictionValidatorStrategyTest {

    private static final RestrictionRoute RESTRICTION_ROUTE_1 = prepareRestrictionRoute(HttpMethod.POST, "/**/users/register");
    private static final RestrictionRoute RESTRICTION_ROUTE_2 = prepareRestrictionRoute(HttpMethod.PUT, "/**/users/update");
    private static final String CAPTCHA_RESPONSE_HEADER = "X-Captcha-Response";
    private static final String SECRET = "secret-key";
    private static final String PATH_USERS_REGISTER = "/users/register";
    private static final String PATH_ANY_ROUTE = "/any/route";
    private static final String CAPTCHA_RESPONSE = "captcha-response";
    private static final String REMOTE_ADDRESS = "remote-address";

    @Mock
    private ReCaptchaValidationService reCaptchaValidationService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ReCaptchaRestrictionValidatorStrategy reCaptchaRestrictionValidatorStrategy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        initStrategy();
        given(request.getHeader(HEADER_CLIENT_ID)).willReturn("test-client");
    }

    @Test
    public void shouldInitSetRoutesAsEmptyList() {

        // given
        ReCaptchaRestrictionValidatorStrategy strategyToInit = new ReCaptchaRestrictionValidatorStrategy(null);

        // when
        strategyToInit.init();

        // then
        assertThat(strategyToInit.getRoutes(), notNullValue());
    }

    @Test
    @Parameters(source = ReCaptchaRestrictionValidatorStrategyParameterProvider.class, method = "skipValidation")
    public void shouldNotValidateRequest(HttpMethod currentMethod, String currentPath) {

        // given
        given(request.getMethod()).willReturn(currentMethod.name());
        given(request.getRequestURI()).willReturn(currentPath);

        // when
        reCaptchaRestrictionValidatorStrategy.validate(request);

        // then
        verify(request, never()).getHeader(CAPTCHA_RESPONSE_HEADER);
        verifyZeroInteractions(reCaptchaValidationService);
    }

    @Test
    public void shouldSuccessfullyValidateRequest() {

        // given
        given(request.getMethod()).willReturn(HttpMethod.POST.name());
        given(request.getRequestURI()).willReturn(PATH_USERS_REGISTER);
        given(request.getHeader(CAPTCHA_RESPONSE_HEADER)).willReturn(CAPTCHA_RESPONSE);
        given(request.getRemoteAddr()).willReturn(REMOTE_ADDRESS);
        given(reCaptchaValidationService.isValid(any(ReCaptchaRequest.class))).willReturn(true);

        // when
        reCaptchaRestrictionValidatorStrategy.validate(request);

        // then
        verify(reCaptchaValidationService).isValid(ReCaptchaRequest.getBuilder()
                .withSecret(SECRET)
                .withResponse(CAPTCHA_RESPONSE)
                .withRemoteIp(REMOTE_ADDRESS)
                .build());
    }

    @Test(expected = ClientSecurityViolationException.class)
    public void shouldValidationFailForMissingCaptchaHeader() {

        // given
        given(request.getMethod()).willReturn(HttpMethod.POST.name());
        given(request.getRequestURI()).willReturn(PATH_USERS_REGISTER);
        given(request.getHeader(CAPTCHA_RESPONSE_HEADER)).willReturn(null);

        // when
        reCaptchaRestrictionValidatorStrategy.validate(request);

        // then
        // exception expected
    }

    @Test(expected = ClientSecurityViolationException.class)
    public void shouldValidationFailForInvalidResponseToken() {

        // given
        given(request.getMethod()).willReturn(HttpMethod.POST.name());
        given(request.getRequestURI()).willReturn(PATH_USERS_REGISTER);
        given(request.getHeader(CAPTCHA_RESPONSE_HEADER)).willReturn(CAPTCHA_RESPONSE);
        given(request.getRemoteAddr()).willReturn(REMOTE_ADDRESS);
        given(reCaptchaValidationService.isValid(any(ReCaptchaRequest.class))).willReturn(false);

        // when
        reCaptchaRestrictionValidatorStrategy.validate(request);

        // then
        // exception expected
    }

    @Test
    public void shouldRestrictionTypeBeCaptchaToken() {

        // when
        RestrictionType result = reCaptchaRestrictionValidatorStrategy.forRestrictionType();

        // then
        assertThat(result, equalTo(RestrictionType.CAPTCHA_TOKEN));
    }

    private void initStrategy() {
        reCaptchaRestrictionValidatorStrategy.setRoutes(Arrays.asList(RESTRICTION_ROUTE_1, RESTRICTION_ROUTE_2));
        reCaptchaRestrictionValidatorStrategy.setSecret(SECRET);
    }

    private static RestrictionRoute prepareRestrictionRoute(HttpMethod method, String path) {

        RestrictionRoute restrictionRoute = new RestrictionRoute();
        restrictionRoute.setMethod(method);
        restrictionRoute.setPath(path);

        return restrictionRoute;
    }

    public static class ReCaptchaRestrictionValidatorStrategyParameterProvider {

        public static Object[] skipValidation() {
            return new Object[] {
                                  // method         // current path
                    new Object[] {HttpMethod.GET,   PATH_ANY_ROUTE},
                    new Object[] {HttpMethod.POST,  PATH_ANY_ROUTE},
                    new Object[] {HttpMethod.PUT,   PATH_ANY_ROUTE},
                    new Object[] {HttpMethod.GET,   PATH_USERS_REGISTER},
                    new Object[] {HttpMethod.PUT,   PATH_USERS_REGISTER}
            };
        }
    }
}