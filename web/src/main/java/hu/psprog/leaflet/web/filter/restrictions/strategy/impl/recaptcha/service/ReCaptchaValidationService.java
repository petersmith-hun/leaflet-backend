package hu.psprog.leaflet.web.filter.restrictions.strategy.impl.recaptcha.service;

import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.web.filter.ClientAcceptorFilter;

/**
 * Validates a captcha-protected request with Google ReCaptcha service.
 * Validation should be called from {@link ClientAcceptorFilter} to prevent processing malicious requests.
 *
 * @author Peter Smith
 */
public interface ReCaptchaValidationService {

    /**
     * Validates given {@link ReCaptchaRequest}.
     *
     * @param reCaptchaRequest {@link ReCaptchaRequest} object to validate
     * @return {@code true} if given request is valid, {@code false otherwise}
     */
    boolean isValid(ReCaptchaRequest reCaptchaRequest);
}
