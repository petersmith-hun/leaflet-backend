package hu.psprog.leaflet.web.filter.restrictions.strategy.impl;

import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionRoute;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.exception.ClientSecurityViolationException;
import hu.psprog.leaflet.web.filter.restrictions.strategy.RestrictionValidatorStrategy;
import hu.psprog.leaflet.web.filter.restrictions.strategy.impl.recaptcha.service.ReCaptchaValidationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static hu.psprog.leaflet.web.filter.ClientAcceptorFilter.HEADER_CLIENT_ID;

/**
 * {@link RestrictionValidatorStrategy} implementation for ReCaptcha validation.
 * Strategy is called if request processing from identified client requires captcha validation ({@link RestrictionType#CAPTCHA_TOKEN} is set).
 * Captcha processing can be configured via {@code leaflet-link.captcha} configuration parameter, which has two keys:
 *  - secret: private key for Google ReCaptcha service
 *  - routes: list of applicable routes as {@link RestrictionRoute} entries.
 *    Method is an enum constant of {@link HttpMethod} enum and route should be an {@link AntPathMatcher} compatible string.
 * For every configured route, captcha validation expects the {@code X-Captcha-Response} request header to be populated for validation.
 * If the header value mentioned above is not present in the request, it is immediately rejected.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "leaflet-link.captcha")
public class ReCaptchaRestrictionValidatorStrategy implements RestrictionValidatorStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReCaptchaRestrictionValidatorStrategy.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String CAPTCHA_RESPONSE_HEADER = "X-Captcha-Response";
    private static final int STRIP_AT_LENGTH = 30;
    private static final String STRIPPED_PART_INDICATOR = "...";

    private ReCaptchaValidationService reCaptchaValidationService;
    private String secret;
    private List<RestrictionRoute> routes;

    @Autowired
    public ReCaptchaRestrictionValidatorStrategy(ReCaptchaValidationService reCaptchaValidationService) {
        this.reCaptchaValidationService = reCaptchaValidationService;
    }

    @PostConstruct
    public void init() {
        if (Objects.isNull(routes)) {
            routes = Collections.emptyList();
        }
    }

    @Override
    public void validate(HttpServletRequest request) throws ClientSecurityViolationException {

        if (isCaptchaValidationRequired(request)) {

            String reCaptchaResponse = request.getHeader(CAPTCHA_RESPONSE_HEADER);
            if (!StringUtils.isEmpty(reCaptchaResponse)) {

                LOGGER.info("Performing ReCaptcha validation for token [{}] (request origin is [{}])", stripResponseToken(reCaptchaResponse), request.getRemoteAddr());
                ReCaptchaRequest reCaptchaRequest = ReCaptchaRequest.getBuilder()
                        .withResponse(reCaptchaResponse)
                        .withSecret(secret)
                        .withRemoteIp(request.getRemoteAddr())
                        .build();

                if (!reCaptchaValidationService.isValid(reCaptchaRequest)) {
                    LOGGER.error("ReCaptcha validation failed for request from client [{}]", request.getHeader(HEADER_CLIENT_ID));
                    throw new ClientSecurityViolationException();
                }

            } else {
                LOGGER.error("Missing mandatory ReCaptcha response in request from client [{}]", request.getHeader(HEADER_CLIENT_ID));
                throw new ClientSecurityViolationException();
            }
        }
    }

    @Override
    public RestrictionType forRestrictionType() {
        return RestrictionType.CAPTCHA_TOKEN;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<RestrictionRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RestrictionRoute> routes) {
        this.routes = routes;
    }

    public static String stripResponseToken(String response) {
        return StringUtils.left(response, STRIP_AT_LENGTH) + STRIPPED_PART_INDICATOR;
    }

    private boolean isCaptchaValidationRequired(HttpServletRequest request) {
        return routes.stream()
                .anyMatch(restrictionRoute -> isRouteMatching(restrictionRoute, request) && isMethodMatching(restrictionRoute, request));
    }

    private boolean isRouteMatching(RestrictionRoute restrictionRoute, HttpServletRequest request) {
        return PATH_MATCHER.match(restrictionRoute.getPath(), request.getRequestURI());
    }

    private boolean isMethodMatching(RestrictionRoute restrictionRoute, HttpServletRequest request) {
        return request.getMethod().equals(restrictionRoute.getMethod().name());
    }
}
