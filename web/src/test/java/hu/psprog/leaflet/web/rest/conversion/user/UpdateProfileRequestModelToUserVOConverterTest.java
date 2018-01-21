package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UpdateProfileRequestModelToUserVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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