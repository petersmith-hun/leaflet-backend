package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UserToUserVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserToUserVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UserToUserVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        UserVO result = converter.convert(USER);

        // then
        assertThat(result, equalTo(USER_VO));
    }
}