package hu.psprog.leaflet.web.filter.restrictions.strategy.impl;

import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.exception.ClientSecurityViolationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static hu.psprog.leaflet.security.jwt.filter.JWTAuthenticationFilter.DEVICE_ID_HEADER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link DeviceIDRestrictionValidatorStrategy}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DeviceIDRestrictionValidatorStrategyTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private DeviceIDRestrictionValidatorStrategy deviceIDRestrictionValidatorStrategy;

    @Test
    public void shouldValidateSuccessfully() {

        // given
        given(request.getHeader(DEVICE_ID_HEADER)).willReturn(UUID.randomUUID().toString());

        // when
        deviceIDRestrictionValidatorStrategy.validate(request);

        // then
        // silent success expected
    }

    @Test(expected = ClientSecurityViolationException.class)
    public void shouldFailToValidate() {

        // given
        given(request.getHeader(DEVICE_ID_HEADER)).willReturn(StringUtils.EMPTY);

        // when
        deviceIDRestrictionValidatorStrategy.validate(request);

        // then
        // exception expected
    }

    @Test
    public void shouldRestrictionTypeReturnDeviceID() {

        // when
        RestrictionType result = deviceIDRestrictionValidatorStrategy.forRestrictionType();

        // then
        assertThat(result, equalTo(RestrictionType.DEVICE_ID));
    }
}