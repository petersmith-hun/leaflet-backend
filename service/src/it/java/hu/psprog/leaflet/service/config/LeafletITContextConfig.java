package hu.psprog.leaflet.service.config;

import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Context configuration for integration tests.
 *
 * @author Peter Smith
 */
@Profile(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
@SpringBootApplication
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.service",
        "hu.psprog.leaflet.persistence.dao"
})
@EnableJpaRepositories(basePackages = LeafletITContextConfig.REPOSITORY_PACKAGE)
@EntityScan(basePackages = LeafletITContextConfig.ENTITY_PACKAGE)
@EnableTransactionManagement
public class LeafletITContextConfig {

    static final String ENTITY_PACKAGE = "hu.psprog.leaflet.persistence.entity";
    static final String REPOSITORY_PACKAGE = "hu.psprog.leaflet.persistence.repository";

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
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // suppressing warning: NoOp password encoding is only used in tests
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public ConversionService conversionService(List<Converter<?, ?>> converters) {

        DefaultConversionService conversionService = new DefaultConversionService();
        converters.forEach(conversionService::addConverter);

        return conversionService;
    }

    @Bean
    public RequestAuthentication requestAuthentication() {
        return Map::of;
    }

    @Bean
    public HttpServletRequest httpServletRequest() {
        return Mockito.mock(HttpServletRequest.class);
    }

    @Bean
    public HttpServletResponse httpServletResponse() {
        return Mockito.mock(HttpServletResponse.class);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return Mockito.mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
        return Mockito.mock(OAuth2AuthorizedClientRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService() {
        return Mockito.mock(OAuth2AuthorizedClientService.class);
    }

    @Bean
    public BridgeClient lens() {
        return Mockito.mock(BridgeClient.class);
    }
}
