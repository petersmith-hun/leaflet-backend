package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link UserVOToExtendedUserDataModelListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserVOToExtendedUserDataModelListConverterTest extends ConversionTestObjects {

    @Mock
    private UserVOToExtendedUserDataModelEntityConverter entityConverter;

    @InjectMocks
    private UserVOToExtendedUserDataModelListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(USER_VO)).willReturn(EXTENDED_USER_DATA_MODEL);

        // when
        UserListDataModel result = converter.convert(Collections.singletonList(USER_VO));

        // then
        assertThat(result, equalTo(USER_LIST_DATA_MODEL));
    }
}