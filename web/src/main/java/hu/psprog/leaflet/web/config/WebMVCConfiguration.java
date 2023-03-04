package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.web.interceptor.ResponseFillerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration for REST web interface.
 *
 * @author Peter Smith
 */
@Configuration
public class WebMVCConfiguration implements WebMvcConfigurer {

    private final ResponseFillerInterceptor responseFillerInterceptor;

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
}
