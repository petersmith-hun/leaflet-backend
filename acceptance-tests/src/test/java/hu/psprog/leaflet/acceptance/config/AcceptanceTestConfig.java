package hu.psprog.leaflet.acceptance.config;

import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static hu.psprog.leaflet.bridge.config.BridgeConfiguration.DEVICE_ID_HEADER;

/**
 * Acceptance test mock context configuration.
 *
 * @author Peter Smith
 */
@TestConfiguration
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.security",
        "hu.psprog.leaflet.bridge"})
@Profile("acceptance")
@ConfigurationProperties(prefix = "test-auth")
public class AcceptanceTestConfig {

    private static final String HEADER_PARAMETER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_SCHEMA = "Bearer {0}";
    private static final String INIT_SCRIPT = "classpath:jwt_session_store_init.sql";
    private static final String DATABASE_NAME = "acceptance-session-store";
    private static final boolean TEST_USER_ENABLED = true;
    private static final long TEST_USER_ID = 1L;
    private static final String IS_AJAX_REQUEST = "IS_AJAX_REQUEST";

    @Autowired
    private JWTComponent jwtComponent;

    @Autowired
    private SessionStoreService sessionStoreService;

    private String username;
    private String realName;
    private String authority;
    private String remoteAddress;
    private UUID deviceId;

    @Bean
    public RequestAuthentication requestAuthentication() {

        String token = jwtComponent.generateToken(new ExtendedUserDetails.Builder()
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

        return () -> {
            Map<String, String> authenticationHeader = new HashMap<>();
            authenticationHeader.put(HEADER_PARAMETER_AUTHORIZATION, MessageFormat.format(AUTHORIZATION_SCHEMA, token));

            return authenticationHeader;
        };
    }

    @Bean
    public HttpServletRequest httpServletRequest() {

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setAttribute(IS_AJAX_REQUEST, true);
        httpServletRequest.setAttribute(DEVICE_ID_HEADER, deviceId);
        httpServletRequest.setRemoteAddr(remoteAddress);

        return httpServletRequest;
    }

    @Bean
    public HttpServletResponse httpServletResponse() {
        return Mockito.mock(HttpServletResponse.class);
    }

    @Bean
    public NamedParameterJdbcTemplate sessionStoreJDBCTemplate() {
        return new NamedParameterJdbcTemplate(sessionStoreDataSource());
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

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    private DataSource sessionStoreDataSource() {

        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName(DATABASE_NAME)
                .addScript(INIT_SCRIPT)
                .build();
    }
}
