package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UserVOToExtendedUserDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserVOToExtendedUserDataModelEntityConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UserVOToExtendedUserDataModelEntityConverter converter;

    @Test
    public void shouldConvert() {

        // when
        ExtendedUserDataModel result = converter.convert(USER_VO);

        // then
        assertThat(result, equalTo(EXTENDED_USER_DATA_MODEL));
    }
}