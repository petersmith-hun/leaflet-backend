package hu.psprog.leaflet.acceptance.config;

import com.nimbusds.jose.JWSAlgorithm;
import hu.psprog.leaflet.acceptance.mock.MockNotificationService;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.impl.BridgeClientImpl;
import hu.psprog.leaflet.bridge.client.impl.InvocationFactoryConfig;
import hu.psprog.leaflet.service.NotificationService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Acceptance test mock context configuration.
 *
 * @author Peter Smith
 */
@TestConfiguration
@Import(InvocationFactoryConfig.class)
@ComponentScan(basePackages = {"hu.psprog.leaflet.bridge"})
@Profile("acceptance")
public class AcceptanceTestConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void executeDataSQL() throws SQLException {

        DataSource dataSource = applicationContext.getBean(DataSource.class);
        ClassPathResource dataSQL = new ClassPathResource("data.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), dataSQL);
    }

    @Bean
    @Primary
    public JsonMapper jsonMapper() {

        return new JsonMapper(JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    @Bean
    public TestRestTemplate testRestTemplate() {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(HttpClientBuilder.create()
                .disableAuthCaching()
                .disableAutomaticRetries()
                .disableConnectionState()
                .disableCookieManagement()
                .disableRedirectHandling()
                .evictIdleConnections(TimeValue.ofSeconds(3L))
                .build());

        return new TestRestTemplate(new RestTemplateBuilder().requestFactory(() -> requestFactory));
    }

    @Bean
    public HttpServletResponse httpServletResponse() {
        return Mockito.mock(HttpServletResponse.class);
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // suppressing warning: NoOp password encoding is only used for acceptance tests
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public NotificationService notificationService() {
        return new MockNotificationService();
    }

    @Bean
    public String jwtSecret(@Value("${test-auth.jwt-secret}") String jwtSecret) {
        return jwtSecret;
    }

    @Bean
    public JwtDecoder jwtDecoder(String jwtSecret) {

        return NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(jwtSecret.getBytes(), JWSAlgorithm.HS256.getName()))
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return Mockito.mock(ClientRegistrationRepository.class);
    }

    @Bean
    public BridgeClient lens() {
        return Mockito.mock(BridgeClientImpl.class);
    }
}
