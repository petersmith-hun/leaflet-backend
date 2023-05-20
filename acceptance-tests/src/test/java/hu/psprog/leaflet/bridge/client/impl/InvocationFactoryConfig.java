package hu.psprog.leaflet.bridge.client.impl;

import hu.psprog.leaflet.bridge.client.handler.InvocationFactory;
import hu.psprog.leaflet.bridge.client.request.RequestAdapter;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.bridge.integration.request.adapter.HttpServletBasedRequestAdapter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private String remoteAddress;
    private UUID deviceId;

    @Bean
    @Primary
    public RequestAuthentication requestAuthentication(AccessTokenTestUtility accessTokenTestUtility) {
        return new MockedRequestAuthentication(accessTokenTestUtility.generateToken());
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

    private HttpServletRequest mockedHttpServletRequest() {

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setAttribute(DEVICE_ID_HEADER, deviceId);
        httpServletRequest.setRemoteAddr(remoteAddress);

        return httpServletRequest;
    }

    public static class MockedRequestAuthentication implements RequestAuthentication {

        private final String token;

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
