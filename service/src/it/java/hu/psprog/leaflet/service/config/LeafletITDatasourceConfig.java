package hu.psprog.leaflet.service.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Datasource configuration for service layer integration tests.
 *
 * @author Peter Smith
 */
@Configuration
@EnableJpaRepositories(basePackages = LeafletITDatasourceConfig.REPOSITORY_PACKAGE)
public class LeafletITDatasourceConfig {

    public static final String ENTITY_PACKAGE = "hu.psprog.leaflet.persistence.entity";
    public static final String REPOSITORY_PACKAGE = "hu.psprog.leaflet.persistence.repository";

    @Bean
    public DataSource dataSource() {

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:h2:mem:acceptance;MODE=LEGACY");
        hikariDataSource.setUsername("sa");
        hikariDataSource.setPassword(StringUtils.EMPTY);
        hikariDataSource.setDriverClassName(Driver.class.getName());

        return hikariDataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("generate-ddl", "true");
        jpaProperties.setProperty("defer-datasource-initialization", "true");

        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaProperties(jpaProperties);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setPackagesToScan(ENTITY_PACKAGE);
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);

        return txManager;
    }

}
