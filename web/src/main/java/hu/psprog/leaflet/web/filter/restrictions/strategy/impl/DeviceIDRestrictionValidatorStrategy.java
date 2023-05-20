package hu.psprog.leaflet.web.filter.restrictions.strategy.impl;

import hu.psprog.leaflet.web.filter.restrictions.exception.ClientSecurityViolationException;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.strategy.RestrictionValidatorStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import static hu.psprog.leaflet.web.filter.ClientAcceptorFilter.HEADER_CLIENT_ID;

/**
 * {@link RestrictionValidatorStrategy} implementation to validate if a request contains a proper device ID.
 *
 * @author Peter Smith
 */
@Component
public class DeviceIDRestrictionValidatorStrategy implements RestrictionValidatorStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceIDRestrictionValidatorStrategy.class);
    private static final String DEVICE_ID_HEADER = "X-Device-ID";

    @Override
    public void validate(HttpServletRequest request) throws ClientSecurityViolationException {

        if (StringUtils.isEmpty(request.getHeader(DEVICE_ID_HEADER))) {
            LOGGER.error("Missing mandatory device ID in request from client [{}]", request.getHeader(HEADER_CLIENT_ID));
            throw new ClientSecurityViolationException();
        }
    }

    @Override
    public RestrictionType forRestrictionType() {
        return RestrictionType.DEVICE_ID;
    }
}
