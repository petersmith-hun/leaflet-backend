package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.service.common.RunLevel;
import hu.psprog.leaflet.web.exception.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

@Configuration
@ComponentScan(ApplicationContextConfig.COMPONENT_SCAN)
@EnableAspectJAutoProxy
public class ApplicationContextConfig {

    private static final String APPLICATION_CONFIG_PROPERTY_SOURCE = "applicationConfigPropertySource";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextConfig.class);
    private static final String JNDI_LEAFLET_CONFIG_LOCATION = "java:comp/env/leafletAppConfig";
    private static final boolean LOG_READ_PROPERTIES = true;
    private static final String APP_VERSION_PROPERTY = "${app.version}";
    private static final String APP_BUILD_DATE_PROPERTY = "${app.built}";

    static final String COMPONENT_SCAN = "hu.psprog.leaflet";

    @Value(ConfigurationProperty.RUN_LEVEL)
    private String runLevelName;

    @Bean
    @Autowired
    public ConversionServiceFactoryBean conversionService(final Set<Converter> converters) {

        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(converters);

        return conversionServiceFactoryBean;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer applicationConfigPropertySource() throws InitializationException {

        try {
            PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
            configurer.setLocation(new ClassPathResource("version.properties"));
            File filename = InitialContext.doLookup(JNDI_LEAFLET_CONFIG_LOCATION);
            Properties properties = loadPropertiesFromInputStream(new FileInputStream(filename));
            configurer.setProperties(properties);

            return configurer;

        } catch (NamingException exception) {
            throw new InitializationException("Main application configuration source not found on JNDI. Check server configuration.", exception);

        } catch (IOException exception) {
            throw new InitializationException("Main application configuration file specified by JNDI not found or malformed.", exception);
        }
    }

    @Bean
    @DependsOn(APPLICATION_CONFIG_PROPERTY_SOURCE)
    public RunLevel runLevel() {

        RunLevel runLevel = RunLevel.PRODUCTION;

        switch (runLevelName) {
            case "INIT":
                runLevel = RunLevel.INIT;
                LOGGER.warn(" ** APPLICATION IS RUNNING IN INIT MODE - PLEASE CHANGE BEFORE DEPLOYING TO PRODUCTION **");
                break;
            case "MAINTENANCE":
                runLevel = RunLevel.MAINTENANCE;
                LOGGER.warn(" ** APPLICATION IS RUNNING IN MAINTENANCE MODE, LOGGING LEVEL WILL BE [DEBUG] **");
                break;
            default:
                LOGGER.info("Application is running in production mode.");
                break;
        }

        return runLevel;
    }

    @Bean
    @DependsOn(APPLICATION_CONFIG_PROPERTY_SOURCE)
    public String appVersion(@Value(APP_VERSION_PROPERTY) String version, @Value(APP_BUILD_DATE_PROPERTY) String builtOn) {
        LOGGER.info("Application loaded successfully, running version v{}, built on {}", version, builtOn);

        return version;
    }

    private static Properties loadPropertiesFromInputStream(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(inputStream);

        if(LOG_READ_PROPERTIES) {
            logProperties(properties);
        }

        return properties;
    }

    private static void logProperties(Properties properties) {
        LOGGER.info("------------------------------------------------------------------------------------------ ");
        LOGGER.info(" External application configuration found. Entries: ");
        properties.entrySet().stream()
                .map(e -> String.format("  * %-40s: %s", e.getKey(), e.getValue()))
                .sorted()
                .forEach(LOGGER::info);
        LOGGER.info("------------------------------------------------------------------------------------------ ");
    }
}
