package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.service.vo.AuthRequestVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link LoginRequestModelToAuthenticationRequestVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
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