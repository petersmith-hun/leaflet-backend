package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AuthResponseVOToLoginResponseDataModelConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AuthResponseVOToLoginResponseDataModelConverterTest extends ConversionTestObjects {

    @InjectMocks
    private AuthResponseVOToLoginResponseDataModelConverter converter;

    @Test
    public void shouldConvertWithSuccess() {

        // when
        LoginResponseDataModel result = converter.convert(AUTH_RESPONSE_VO_WITH_SUCCESS);

        // then
        assertThat(result, equalTo(LOGIN_RESPONSE_DATA_MODEL_WITH_SUCCESS));
    }

    @Test
    public void shouldConvertWithFailure() {

        // when
        LoginResponseDataModel result = converter.convert(AUTH_RESPONSE_VO_WITH_FAILURE);

        // then
        assertThat(result, equalTo(LOGIN_RESPONSE_DATA_MODEL_WITH_FAILURE));
    }
}