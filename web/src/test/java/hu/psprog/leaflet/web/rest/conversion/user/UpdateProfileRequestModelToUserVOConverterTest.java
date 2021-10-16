package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UpdateProfileRequestModelToUserVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UpdateProfileRequestModelToUserVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UpdateProfileRequestModelToUserVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        UserVO result = converter.convert(UPDATE_PROFILE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(USER_VO_FOR_UPDATE));
    }
}