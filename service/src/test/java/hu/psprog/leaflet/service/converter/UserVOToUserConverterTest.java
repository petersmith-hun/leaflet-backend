package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UserVOToUserConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserVOToUserConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UserVOToUserConverter converter;

    @Test
    public void shouldConvertFullVO() {

        // when
        User result = converter.convert(USER_VO);

        // then
        assertThat(result, equalTo(USER));
    }

    @Test
    public void shouldConvertMinimumVO() {

        // when
        User result = converter.convert(USER_MINIMUM_VO);

        // then
        assertThat(result, equalTo(USER_MINIMUM));
    }
}