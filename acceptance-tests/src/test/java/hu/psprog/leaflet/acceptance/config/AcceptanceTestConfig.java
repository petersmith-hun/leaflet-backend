package hu.psprog.leaflet.acceptance.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.nimbusds.jose.JWSAlgorithm;
import hu.psprog.leaflet.acceptance.mock.MockNotificationService;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.impl.BridgeClientImpl;
import hu.psprog.leaflet.bridge.client.impl.InvocationFactoryConfig;
import hu.psprog.leaflet.service.NotificationService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.DateFormat;

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
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(DateFormat.getInstance());

        return objectMapper;
    }

    @Bean
    @Primary
    public Client jacksonClient(ObjectMapper objectMapper) {

        return ClientBuilder.newBuilder()
                .register(new JacksonJsonProvider(objectMapper))
                .register(MultiPartFeature.class)
                .build();
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
