package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.web.interceptor.ResponseFillerInterceptor;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration for REST web interface.
 *
 * @author Peter Smith
 */
@Configuration
@ConfigurationProperties(prefix = "cors")
@Setter(AccessLevel.PACKAGE)
public class WebMVCConfiguration implements WebMvcConfigurer {

    private final ResponseFillerInterceptor responseFillerInterceptor;
    private boolean enableCors;
    private String[] allowedOrigins;

    @Autowired
    public WebMVCConfiguration(ResponseFillerInterceptor responseFillerInterceptor) {
        this.responseFillerInterceptor = responseFillerInterceptor;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseFillerInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        if (enableCors) {
            registry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedOrigins(allowedOrigins);
        }
    }
}
