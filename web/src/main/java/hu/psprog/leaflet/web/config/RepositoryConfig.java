package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.persistence.repository.BlogEntryRepository;
import hu.psprog.leaflet.web.exception.InitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = ConfigurationProperty.REPOSITORY_PACKAGE)
@EnableTransactionManagement
public class RepositoryConfig {

    public static final String PROPERTY_KEY_HIBERNATE_DDL_AUTO = "hibernate.hbm2ddl.auto";
    public static final String PROPERTY_KEY_HIBERNATE_DIALECT = "hibernate.dialect";

    @Value(ConfigurationProperty.JNDI_JDBC_SOURCE)
    private String jdbcSource;

    @Value(ConfigurationProperty.DATASOURCE_SHOW_SQL)
    private boolean generateDDL;

    @Value(ConfigurationProperty.DATASOURCE_GENERATE_DDL)
    private boolean showSQL;

    @Value(ConfigurationProperty.HIBERNATE_DDL_AUTO)
    private String hibernateDDLAuto;

    @Value(ConfigurationProperty.HIBERNATE_DIALECT)
    private String hibernateDialect;

    @Bean
    public DataSource dataSource() throws InitializationException {

        try {
            JndiTemplate jndiTemplate = new JndiTemplate();
            DataSource dataSource = (DataSource) jndiTemplate.lookup(jdbcSource);

            // check if connection is alive
            dataSource.getConnection();

            return dataSource;

        } catch (SQLException | NamingException e) {
            throw new InitializationException(e);
        }
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() throws InitializationException {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

        Properties properties = new Properties();
        properties.put(PROPERTY_KEY_HIBERNATE_DDL_AUTO, hibernateDDLAuto);
        properties.put(PROPERTY_KEY_HIBERNATE_DIALECT, hibernateDialect);

        adapter.setShowSql(generateDDL);
        adapter.setGenerateDdl(showSQL);

        factoryBean.setJpaProperties(properties);
        factoryBean.setJpaVendorAdapter(adapter);
        factoryBean.setPackagesToScan(ConfigurationProperty.ENTITY_PACKAGE);
        factoryBean.setDataSource(dataSource());
        factoryBean.setPersistenceProvider(adapter.getPersistenceProvider());
        factoryBean.setJpaDialect(new HibernateJpaDialect());
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws InitializationException {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());

        return txManager;
    }
}
