package hu.psprog.leaflet.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.web.interceptor.ResponseWrapperInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
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

    @Bean
    public HandlerInterceptor responseWrapperInterceptor() {
        return new ResponseWrapperInterceptor();
    }

    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

        ViewResolver jsonViewResolver = (viewName, locale) -> new MappingJackson2JsonView();
        registry.viewResolver(jsonViewResolver);

        super.configureViewResolvers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseWrapperInterceptor());
        super.addInterceptors(registry);
    }
}
