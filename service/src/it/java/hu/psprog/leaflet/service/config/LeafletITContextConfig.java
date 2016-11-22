package hu.psprog.leaflet.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.service.common.RunLevel;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Arrays;
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
public class LeafletITContextConfig {

    public static final String INTEGRATION_TEST_DB_SCRIPT_USERS = "classpath:/service_it_db_script_users.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ENTRIES = "classpath:/service_it_db_script_entries.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_CATEGORIES = "classpath:/service_it_db_script_categories.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_COMMENTS = "classpath:/service_it_db_script_comments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS = "classpath:/service_it_db_script_documents.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS = "classpath:/service_it_db_script_attachments.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_TAGS = "classpath:/service_it_db_script_tags.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DCP = "classpath:/service_it_db_script_dcp.sql";

    public static final String REPOSITORY_PACKAGE = "hu.psprog.leaflet.persistence.repository";
    public static final String ENTITY_PACKAGE = "hu.psprog.leaflet.persistence.entity";
    public static final String COMPONENT_SCAN_PACKAGE = "hu.psprog.leaflet.service";
    public static final String INTEGRATION_TEST_CONFIG_PROFILE = "it";

    @Autowired
    private UserDetailsService userDetailsService;

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
    public RunLevel runLevel() {

        return RunLevel.INIT;
    }

    @Bean
    public TestObjectReader testObjectReader() {

        return new TestObjectReader();
    }

    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper();
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
}
