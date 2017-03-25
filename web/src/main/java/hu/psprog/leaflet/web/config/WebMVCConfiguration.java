package hu.psprog.leaflet.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.web.interceptor.ResponseFillerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

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

    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

        ViewResolver jsonViewResolver = (viewName, locale) -> new MappingJackson2JsonView();
        registry.viewResolver(jsonViewResolver);

        super.configureViewResolvers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseFillerInterceptor);
        super.addInterceptors(registry);
    }
}
