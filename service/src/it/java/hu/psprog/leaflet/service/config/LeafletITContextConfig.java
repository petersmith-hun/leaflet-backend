package hu.psprog.leaflet.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.domain.SessionStoreValidationStatus;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Context configuration for integration tests.
 *
 * @author Peter Smith
 */
@Profile(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
@Configuration
@Import(LeafletITDatasourceConfig.class)
@ComponentScan(basePackages = LeafletITContextConfig.COMPONENT_SCAN_PACKAGE)
public class LeafletITContextConfig implements ApplicationListener<ContextClosedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeafletITContextConfig.class);

    public static final String INTEGRATION_TEST_DB_SCRIPT_USERS = "classpath:/service_it_db_script_users.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ENTRIES = "classpath:/service_it_db_script_entries.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_CATEGORIES = "classpath:/service_it_db_script_categories.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_COMMENTS = "classpath:/service_it_db_script_comments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS = "classpath:/service_it_db_script_documents.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS = "classpath:/service_it_db_script_attachments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_TAGS = "classpath:/service_it_db_script_tags.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DCP = "classpath:/service_it_db_script_dcp.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES = "classpath:/service_it_db_script_front_end_routes.sql";

    public static final String COMPONENT_SCAN_PACKAGE = "hu.psprog.leaflet.service";
    public static final String INTEGRATION_TEST_CONFIG_PROFILE = "it";

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {

        Properties properties = new Properties();
        properties.setProperty("files.upload.storage-path", "${java.io.tmpdir}");
        properties.setProperty("mail.event.comment-notification.enabled", "false");
        properties.setProperty("sitemap.server-name", "http://localhost:9999");

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setProperties(properties);

        return configurer;
    }

    @Bean
    public EventNotificationServiceClient eventNotificationServiceClient() {
        return Mockito.mock(EventNotificationServiceClient.class);
    }

    @Bean
    public SessionStoreService sessionStoreServiceStub() {
        return new SessionStoreService() {
            @Override
            public void storeToken(ClaimedTokenContext claimedTokenContext) {

            }

            @Override
            public SessionStoreValidationStatus validateToken(JWTAuthenticationToken jwtAuthenticationToken) {
                return null;
            }

            @Override
            public void revokeToken(JWTAuthenticationToken jwtAuthenticationToken) {

            }

            @Override
            public void cleanExpiredToken(int i) {

            }
        };
    }

    @Bean
    public TestObjectReader testObjectReader() {

        return new TestObjectReader();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule timestampDeserialization = new SimpleModule();
        timestampDeserialization.addDeserializer(Date.class, new DateDeserializers.TimestampDeserializer());
        objectMapper.registerModule(timestampDeserialization);
        return objectMapper;
    }

    @Bean
    public JWTComponent jwtUtility() {
        return new JWTComponentStub();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // suppressing warning: NoOp password encoding is only used in tests
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Autowired
    public ConversionService conversionService(List<Converter<?, ?>> converters) {

        DefaultConversionService conversionService = new DefaultConversionService();
        converters.forEach(conversionService::addConverter);

        return conversionService;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

        File temporalFileStorage = event.getApplicationContext()
                .getBean("fileStorage", File.class);

        FileSystemUtils.deleteRecursively(temporalFileStorage);

        if (temporalFileStorage.exists()) {
            LOGGER.warn("Failed to delete temporal file storage at '{}'", temporalFileStorage.getAbsolutePath());
        }
    }
}
