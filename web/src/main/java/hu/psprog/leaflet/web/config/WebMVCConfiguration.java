package hu.psprog.leaflet.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.web.interceptor.ResponseFillerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.MultipartConfigElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Spring MVC configuration for REST web interface.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebMvc
public class WebMVCConfiguration extends WebMvcConfigurerAdapter {

    public static final String IS_AJAX_REQUEST = "isAjax";

    @Autowired
    private ResponseFillerInterceptor responseFillerInterceptor;

    private ObjectMapper objectMapper;

    @Bean
    public ObjectMapper objectMapper() {
        objectMapper = new ObjectMapper();
        return objectMapper;
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
    public void configureViewResolvers(ViewResolverRegistry registry) {

        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setObjectMapper(objectMapper);
        mappingJackson2JsonView.setModelKeys(getModelKeys());
        ViewResolver jsonViewResolver = (viewName, locale) -> mappingJackson2JsonView;
        registry.viewResolver(jsonViewResolver);

        super.configureViewResolvers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseFillerInterceptor);
        super.addInterceptors(registry);
    }

    private Set<String> getModelKeys() {
        Set<String> modelKeys = new HashSet<>();
        modelKeys.addAll(Arrays.asList("body", "seo", "pagination", "error"));
        return modelKeys;
    }
}
