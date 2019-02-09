package hu.psprog.leaflet.acceptance.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import hu.psprog.leaflet.acceptance.mock.MockNotificationService;
import hu.psprog.leaflet.bridge.client.impl.InvocationFactoryConfig;
import hu.psprog.leaflet.service.NotificationService;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.text.DateFormat;

/**
 * Acceptance test mock context configuration.
 *
 * @author Peter Smith
 */
@TestConfiguration
@Import(InvocationFactoryConfig.class)
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.security",
        "hu.psprog.leaflet.bridge"})
@Profile("acceptance")
public class AcceptanceTestConfig {

    private static final String INIT_SCRIPT = "classpath:jwt_session_store_init.sql";
    private static final String DATABASE_NAME = "acceptance-session-store";

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
    public NamedParameterJdbcTemplate sessionStoreJDBCTemplate() {
        return new NamedParameterJdbcTemplate(sessionStoreDataSource());
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

    private DataSource sessionStoreDataSource() {

        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName(DATABASE_NAME)
                .addScript(INIT_SCRIPT)
                .build();
    }
}
