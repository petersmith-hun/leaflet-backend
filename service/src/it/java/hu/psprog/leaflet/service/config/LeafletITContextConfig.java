package hu.psprog.leaflet.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hu.psprog.leaflet.mail.client.MailClient;
import hu.psprog.leaflet.mail.config.MailProcessorConfigurationProperties;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.mail.domain.MailDeliveryInfo;
import hu.psprog.leaflet.mail.domain.MailDeliveryStatus;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.sessionstore.domain.ClaimedTokenContext;
import hu.psprog.leaflet.security.sessionstore.domain.SessionStoreValidationStatus;
import hu.psprog.leaflet.security.sessionstore.service.SessionStoreService;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import io.reactivex.Observable;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
@ComponentScan(basePackages = LeafletITContextConfig.COMPONENT_SCAN_PACKAGE)
@EnableJpaRepositories(basePackages = LeafletITContextConfig.REPOSITORY_PACKAGE)
@EnableGlobalMethodSecurity(prePostEnabled = false)
public class LeafletITContextConfig implements ApplicationListener<ContextClosedEvent> {

    public static final String INTEGRATION_TEST_DB_SCRIPT_USERS = "classpath:/service_it_db_script_users.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ENTRIES = "classpath:/service_it_db_script_entries.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_CATEGORIES = "classpath:/service_it_db_script_categories.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_COMMENTS = "classpath:/service_it_db_script_comments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS = "classpath:/service_it_db_script_documents.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS = "classpath:/service_it_db_script_attachments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_TAGS = "classpath:/service_it_db_script_tags.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DCP = "classpath:/service_it_db_script_dcp.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_FRONT_END_ROUTES = "classpath:/service_it_db_script_front_end_routes.sql";

    public static final String REPOSITORY_PACKAGE = "hu.psprog.leaflet.persistence.repository";
    public static final String ENTITY_PACKAGE = "hu.psprog.leaflet.persistence.entity";
    public static final String COMPONENT_SCAN_PACKAGE = "hu.psprog.leaflet.service";
    public static final String INTEGRATION_TEST_CONFIG_PROFILE = "it";

    @Autowired
    private UserDetailsService userDetailsService;

    private TemporaryFolder temporaryFolder;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {

        Properties properties = new Properties();
        properties.setProperty("files.upload.storage-path", "${java.io.tmpdir}");
        properties.setProperty("mail.event.comment-notification.enabled", "false");

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setProperties(properties);

        return configurer;
    }

    @Bean
    public MailClient mailClient() {
        return mail -> Observable.just(MailDeliveryInfo.getBuilder()
                .withMail(Mail.getBuilder().build())
                .withConstraintViolations(Collections.emptyMap())
                .withMailDeliveryStatus(MailDeliveryStatus.DELIVERED)
                .build());
    }

    @Bean
    public MailProcessorConfigurationProperties mailProcessorConfigurationProperties() {
        return new MailProcessorConfigurationProperties(null, null, null, null);
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
    public DataSource dataSource() {

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();

        return builder.setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaProperties(jpaProperties);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setPackagesToScan(ENTITY_PACKAGE);
        factoryBean.setDataSource(dataSource());
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());

        return txManager;
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

        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }

    @Bean
    public File fileStorage() throws IOException {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        return temporaryFolder.getRoot();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
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
        temporaryFolder.delete();
    }
}
