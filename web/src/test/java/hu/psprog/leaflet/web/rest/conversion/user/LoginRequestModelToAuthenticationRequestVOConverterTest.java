package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.service.vo.AuthRequestVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link LoginRequestModelToAuthenticationRequestVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginRequestModelToAuthenticationRequestVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private LoginRequestModelToAuthenticationRequestVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        AuthRequestVO result = converter.convert(LOGIN_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(AUTH_REQUEST_VO));
    }
}