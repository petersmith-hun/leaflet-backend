package hu.psprog.leaflet.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class RepositoryConfig {

    private static final String REPOSITORY_PACKAGE = "hu.psprog.leaflet.persistence.repository";

    @Value("${jndi.jdbc.source}")
    private String jdbcSource;

    @Value("${datasource.showSql}")
    private boolean generateDDL;

    @Value("${datasource.generateDdl}")
    private boolean showSQL;

    @Bean
    public DataSource dataSource() throws NamingException, SQLException {

        JndiTemplate jndiTemplate = new JndiTemplate();
        DataSource dataSource = (DataSource) jndiTemplate.lookup(jdbcSource);

        // check if connection is alive
        dataSource.getConnection();

        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() throws NamingException, SQLException {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

        adapter.setShowSql(generateDDL);
        adapter.setGenerateDdl(showSQL);

        factoryBean.setJpaVendorAdapter(adapter);
        factoryBean.setPackagesToScan(REPOSITORY_PACKAGE);
        factoryBean.setDataSource(dataSource());
        factoryBean.setPersistenceProvider(adapter.getPersistenceProvider());
        factoryBean.setJpaDialect(new HibernateJpaDialect());
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws NamingException, SQLException {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());

        return txManager;
    }
}
