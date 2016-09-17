package hu.psprog.leaflet.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.service.common.RunLevel;
import hu.psprog.leaflet.service.helper.TestObjectReader;
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
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
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

    public static final String REPOSITORY_PACKAGE = "hu.psprog.leaflet.persistence.repository";
    public static final String ENTITY_PACKAGE = "hu.psprog.leaflet.persistence.entity";
    public static final String COMPONENT_SCAN_PACKAGE = "hu.psprog.leaflet.service";
    public static final String INTEGRATION_TEST_CONFIG_PROFILE = "it";

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
}
