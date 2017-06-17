package hu.psprog.leaflet.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.web.interceptor.ResponseFillerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

/**
 * Spring MVC configuration for REST web interface.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebMvc
public class WebMVCConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ResponseFillerInterceptor responseFillerInterceptor;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(@Value(ConfigurationProperty.UPLOADS_DIRECTORY) String storageLocation,
                                                         @Value(ConfigurationProperty.UPLOADS_MAX_SIZE) long maxSize) {
        return new MultipartConfigElement(storageLocation, maxSize, maxSize, (int) maxSize);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseFillerInterceptor);
        super.addInterceptors(registry);
    }
}
