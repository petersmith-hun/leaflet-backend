package hu.psprog.leaflet.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.service",
        "hu.psprog.leaflet.persistence.dao"
})
public class LeafletITContextConfig {

    public static final String INTEGRATION_TEST_DB_SCRIPT_USERS = "classpath:/service_it_db_script_users.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ENTRIES = "classpath:/service_it_db_script_entries.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_CATEGORIES = "classpath:/service_it_db_script_categories.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_COMMENTS = "classpath:/service_it_db_script_comments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS = "classpath:/service_it_db_script_documents.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS = "classpath:/service_it_db_script_attachments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_TAGS = "classpath:/service_it_db_script_tags.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DCP = "classpath:/service_it_db_script_dcp.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES = "classpath:/service_it_db_script_front_end_routes.sql";

    public static final String INTEGRATION_TEST_CONFIG_PROFILE = "it";

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {

        Properties properties = new Properties();
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
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule timestampDeserialization = new SimpleModule();
        timestampDeserialization.addDeserializer(Date.class, new DateDeserializers.TimestampDeserializer());
        objectMapper.registerModule(timestampDeserialization);
        return objectMapper;
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
}
