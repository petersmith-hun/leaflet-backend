package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.web.exception.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;

import java.util.Set;

@Configuration
@EnableAspectJAutoProxy
public class ApplicationContextConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextConfig.class);

    private static final String APPLICATION_CONFIG_PROPERTY_SOURCE = "applicationConfigPropertySource";
    private static final String APP_VERSION_PROPERTY = "${app.version}";
    private static final String INIT_MODE_ENABLED = "${init:false}";

    @Bean
    @Autowired
    @Primary
    public ConversionServiceFactoryBean leafletConversionService(final Set<Converter> converters) {

        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(converters);

        return conversionServiceFactoryBean;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer applicationConfigPropertySource() throws InitializationException {

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("version.properties"));

        return configurer;
    }

    @Bean
    public Boolean initModeEnabled(@Value(INIT_MODE_ENABLED) boolean initModeEnabled) {

        if (initModeEnabled) {
            LOGGER.warn("Application is running in INIT mode! Please change it before deploying to production!");
        }

        return initModeEnabled;
    }

    @Bean
    @DependsOn(APPLICATION_CONFIG_PROPERTY_SOURCE)
    public String appVersion(@Value(APP_VERSION_PROPERTY) String version) {
        return version;
    }
}
