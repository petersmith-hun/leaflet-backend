package hu.psprog.leaflet.web.filter.restrictions.strategy.impl.recaptcha.service.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.recaptcha.api.client.ReCaptchaClient;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaErrorCode;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link ReCaptchaValidationServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class ReCaptchaValidationServiceImplTest {

    private static final ReCaptchaRequest RE_CAPTCHA_REQUEST = ReCaptchaRequest.getBuilder()
            .withResponse("test-response")
            .build();

    @Mock
    private ReCaptchaClient reCaptchaClient;

    @InjectMocks
    private ReCaptchaValidationServiceImpl reCaptchaValidationService;

    @Test
    public void shouldValidationReturnTrue() throws CommunicationFailureException {

        // given
        given(reCaptchaClient.validate(RE_CAPTCHA_REQUEST)).willReturn(ReCaptchaResponse.getBuilder()
                .withSuccess(true)
                .build());

        // when
        boolean result = reCaptchaValidationService.isValid(RE_CAPTCHA_REQUEST);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldValidationReturnFalseIfTokenIsInvalid() throws CommunicationFailureException {

        // given
        given(reCaptchaClient.validate(RE_CAPTCHA_REQUEST)).willReturn(ReCaptchaResponse.getBuilder()
                .withSuccess(false)
                .withErrorCodes(Collections.singletonList(ReCaptchaErrorCode.TIMEOUT_OR_DUPLICATE))
                .build());

        // when
        boolean result = reCaptchaValidationService.isValid(RE_CAPTCHA_REQUEST);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldValidationReturnFalseOnCommunicationError() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(reCaptchaClient).validate(RE_CAPTCHA_REQUEST);

        // when
        boolean result = reCaptchaValidationService.isValid(RE_CAPTCHA_REQUEST);

        // then
        assertThat(result, is(false));
    }
}