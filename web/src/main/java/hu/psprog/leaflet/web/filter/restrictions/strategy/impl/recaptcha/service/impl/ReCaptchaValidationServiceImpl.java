package hu.psprog.leaflet.web.filter.restrictions.strategy.impl.recaptcha.service.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.recaptcha.api.client.ReCaptchaClient;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaResponse;
import hu.psprog.leaflet.web.filter.restrictions.strategy.impl.recaptcha.service.ReCaptchaValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static hu.psprog.leaflet.web.filter.restrictions.strategy.impl.ReCaptchaRestrictionValidatorStrategy.stripResponseToken;

/**
 * Implementation of {@link ReCaptchaValidationService}.
 *
 * @author Peter Smith
 */
@Service
public class ReCaptchaValidationServiceImpl implements ReCaptchaValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReCaptchaValidationServiceImpl.class);

    private ReCaptchaClient reCaptchaClient;

    @Autowired
    public ReCaptchaValidationServiceImpl(ReCaptchaClient reCaptchaClient) {
        this.reCaptchaClient = reCaptchaClient;
    }

    @Override
    public boolean isValid(ReCaptchaRequest reCaptchaRequest) {

        boolean reCaptchaValid = false;
        try {
            ReCaptchaResponse reCaptchaResponse = reCaptchaClient.validate(reCaptchaRequest);
            reCaptchaValid = reCaptchaResponse.isSuccessful();

            if (!reCaptchaValid) {
                LOGGER.warn("ReCaptcha service response for validation token [{}] is {}",
                        stripResponseToken(reCaptchaRequest.getResponse()), reCaptchaResponse.getErrorCodes());
            }

        } catch (CommunicationFailureException e) {
            LOGGER.error("Failed to reach ReCaptcha service", e);
        }

        return reCaptchaValid;
    }
}
