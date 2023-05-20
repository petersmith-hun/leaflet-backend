package hu.psprog.leaflet.web.filter.restrictions.strategy.impl;

import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionRoute;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.exception.ClientSecurityViolationException;
import hu.psprog.leaflet.web.filter.restrictions.strategy.impl.recaptcha.service.ReCaptchaValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Stream;

import static hu.psprog.leaflet.web.filter.ClientAcceptorFilter.HEADER_CLIENT_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link ReCaptchaRestrictionValidatorStrategy}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
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

    @Mock(strictness = Mock.Strictness.LENIENT)
    private HttpServletRequest request;

    @InjectMocks
    private ReCaptchaRestrictionValidatorStrategy reCaptchaRestrictionValidatorStrategy;

    @BeforeEach
    public void setup() {
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

    @ParameterizedTest
    @MethodSource("skipValidationDataProvider")
    public void shouldNotValidateRequest(HttpMethod currentMethod, String currentPath) {

        // given
        given(request.getMethod()).willReturn(currentMethod.name());
        given(request.getRequestURI()).willReturn(currentPath);

        // when
        reCaptchaRestrictionValidatorStrategy.validate(request);

        // then
        verify(request, never()).getHeader(CAPTCHA_RESPONSE_HEADER);
        verifyNoInteractions(reCaptchaValidationService);
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

    @Test
    public void shouldValidationFailForMissingCaptchaHeader() {

        // given
        given(request.getMethod()).willReturn(HttpMethod.POST.name());
        given(request.getRequestURI()).willReturn(PATH_USERS_REGISTER);
        given(request.getHeader(CAPTCHA_RESPONSE_HEADER)).willReturn(null);

        // when
        Assertions.assertThrows(ClientSecurityViolationException.class, () -> reCaptchaRestrictionValidatorStrategy.validate(request));

        // then
        // exception expected
    }

    @Test
    public void shouldValidationFailForInvalidResponseToken() {

        // given
        given(request.getMethod()).willReturn(HttpMethod.POST.name());
        given(request.getRequestURI()).willReturn(PATH_USERS_REGISTER);
        given(request.getHeader(CAPTCHA_RESPONSE_HEADER)).willReturn(CAPTCHA_RESPONSE);
        given(request.getRemoteAddr()).willReturn(REMOTE_ADDRESS);
        given(reCaptchaValidationService.isValid(any(ReCaptchaRequest.class))).willReturn(false);

        // when
        Assertions.assertThrows(ClientSecurityViolationException.class, () -> reCaptchaRestrictionValidatorStrategy.validate(request));

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

    private static Stream<Arguments> skipValidationDataProvider() {

        return Stream.of(
                Arguments.of(HttpMethod.GET,   PATH_ANY_ROUTE),
                Arguments.of(HttpMethod.POST,  PATH_ANY_ROUTE),
                Arguments.of(HttpMethod.PUT,   PATH_ANY_ROUTE),
                Arguments.of(HttpMethod.GET,   PATH_USERS_REGISTER),
                Arguments.of(HttpMethod.PUT,   PATH_USERS_REGISTER)
        );
    }
}