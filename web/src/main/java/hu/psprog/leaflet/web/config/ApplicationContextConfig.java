package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.web.exception.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan(ApplicationContextConfig.COMPONENT_SCAN)
public class ApplicationContextConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextConfig.class);
    private static final String JNDI_LEAFLET_CONFIG_LOCATION = "java:comp/env/leafletAppConfig";
    private static final boolean LOG_READ_PROPERTIES = true;

    protected static final String COMPONENT_SCAN = "hu.psprog.leaflet";

    @Bean
    public static PropertySourcesPlaceholderConfigurer applicationConfigPropertySource() throws InitializationException {

        try {
            PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
            File filename = InitialContext.doLookup(JNDI_LEAFLET_CONFIG_LOCATION);
            Properties properties = loadPropertiesFromInputStream(new FileInputStream(filename));
            configurer.setProperties(properties);

            return configurer;

        } catch (NamingException exception) {
            throw new InitializationException("Main application configuration source not found on JNDI. Check server configuration.");

        } catch (IOException exception) {
            throw new InitializationException("Main application configuration file specified by JNDI not found or malformed.");
        }
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
