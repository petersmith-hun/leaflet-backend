package hu.psprog.leaflet.bridge.client.impl;

import hu.psprog.leaflet.bridge.client.handler.InvocationFactory;
import hu.psprog.leaflet.bridge.client.request.RequestAdapter;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.bridge.integration.request.adapter.HttpServletBasedRequestAdapter;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static hu.psprog.leaflet.bridge.client.domain.BridgeConstants.DEVICE_ID_HEADER;

/**
 * {@link InvocationFactory} bean configuration with mocked context.
 *
 * @author Peter Smith
 */
@Configuration
@ConfigurationProperties(prefix = "test-auth")
public class InvocationFactoryConfig {

    private static final String HEADER_PARAMETER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_SCHEMA = "Bearer {0}";
    private static final boolean TEST_USER_ENABLED = true;
    private static final long TEST_USER_ID = 1L;

    private String username;
    private String realName;
    private String authority;
    private String remoteAddress;
    private UUID deviceId;

    @Autowired
    private JWTComponent jwtComponent;

    @Autowired
    private SessionStoreService sessionStoreService;

    @Bean
    @Primary
    public RequestAuthentication requestAuthentication() {

        String token = jwtComponent.generateToken(ExtendedUserDetails.getBuilder()
                .withAuthorities(AuthorityUtils.createAuthorityList(authority))
                .withEnabled(TEST_USER_ENABLED)
                .withID(TEST_USER_ID)
                .withName(realName)
                .withUsername(username)
                .build()).getToken();

        sessionStoreService.storeToken(ClaimedTokenContext.getBuilder()
                .withToken(token)
                .withRemoteAddress(remoteAddress)
                .withDeviceID(deviceId)
                .build());

        return new MockedRequestAuthentication(token);
    }

    @Bean
    @Primary
    public RequestAdapter requestAdapter(HttpServletResponse httpServletResponse) {
        return new HttpServletBasedRequestAdapter(mockedHttpServletRequest(), httpServletResponse);
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    private HttpServletRequest mockedHttpServletRequest() {

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setAttribute(DEVICE_ID_HEADER, deviceId);
        httpServletRequest.setRemoteAddr(remoteAddress);

        return httpServletRequest;
    }

    public class MockedRequestAuthentication implements RequestAuthentication {

        private String token;

        public MockedRequestAuthentication(String token) {
            this.token = token;
        }

        @Override
        public Map<String, String> getAuthenticationHeader() {

            Map<String, String> authenticationHeader = new HashMap<>();
            authenticationHeader.put(HEADER_PARAMETER_AUTHORIZATION, MessageFormat.format(AUTHORIZATION_SCHEMA, token));

            return authenticationHeader;
        }
    }
}
