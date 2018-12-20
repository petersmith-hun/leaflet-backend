package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.service.vo.LoginContextVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static hu.psprog.leaflet.security.jwt.filter.JWTAuthenticationFilter.DEVICE_ID_HEADER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link LoginContextFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginContextFactoryTest extends ConversionTestObjects {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private LoginContextFactory loginContextFactory;

    @Before
    public void setup() {
        super.setup();
        given(request.getRemoteAddr()).willReturn(REMOTE_ADDRESS);
        given(request.getHeader(DEVICE_ID_HEADER)).willReturn(DEVICE_ID.toString());
    }

    @Test
    public void shouldBuildContextForLogin() {

        // when
        LoginContextVO result = loginContextFactory.forLogin(LOGIN_REQUEST_MODEL, request);

        // then
        assertThat(result, equalTo(LOGIN_CONTEXT_VO_FOR_LOGIN));
    }

    @Test
    public void shouldBuildContextForPasswordReset() {

        // when
        LoginContextVO result = loginContextFactory.forPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, request);

        // then
        assertThat(result, equalTo(LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET));
    }

    @Test
    public void shouldBuildContextForRenewal() {

        // when
        LoginContextVO result = loginContextFactory.forRenewal(request);

        // then
        assertThat(result, equalTo(LOGIN_CONTEXT_VO_FOR_RENEWAL));
    }
}