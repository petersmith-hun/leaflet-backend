package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UserVOToExtendedUserDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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