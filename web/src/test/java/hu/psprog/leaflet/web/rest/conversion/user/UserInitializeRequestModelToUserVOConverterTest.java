package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UserInitializeRequestModelToUserVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserInitializeRequestModelToUserVOConverterTest extends ConversionTestObjects {

    private static final boolean ENABLED_BY_DEFAULT = true;

    private UserInitializeRequestModelToUserVOConverter converter;

    @Before
    public void setup() {
        super.setup();
        converter = new UserInitializeRequestModelToUserVOConverter(ENABLED_BY_DEFAULT, localeConverter);
    }

    @Test
    public void shouldConvertInitializeRequest() {

        // when
        UserVO result = converter.convert(USER_INITIALIZE_REQUEST_MODEL);

        // then
        assertResult(result, Authority.USER);
    }

    @Test
    public void shouldConvertCreateRequest() {

        // when
        UserVO result = converter.convert(USER_CREATE_REQUEST_MODEL);

        // then
        assertResult(result, Authority.ADMIN);
    }

    private void assertResult(UserVO result, GrantedAuthority expectedAppliedAuthority) {
        assertThat(result.getUsername(), equalTo(USERNAME));
        assertThat(result.getEmail(), equalTo(EMAIL));
        assertThat(result.getPassword(), equalTo(PASSWORD));
        assertThat(result.isEnabled(), equalTo(ENABLED_BY_DEFAULT));
        assertThat(result.getLocale(), equalTo(LOCALE));
        assertThat(result.getAuthorities().size(), equalTo(1));
        assertThat(result.getAuthorities().contains(expectedAppliedAuthority), is(true));
    }
}