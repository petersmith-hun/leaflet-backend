package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.web.interceptor.ResponseWrapperInterceptor;
import hu.psprog.leaflet.web.rest.mapper.AutoWrappingJacksonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

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
    public HttpMessageConverter<?> autoWrappingJacksonHttpMessageConverter() {
        return new AutoWrappingJacksonHttpMessageConverter();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(autoWrappingJacksonHttpMessageConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(responseWrapperInterceptor());
    }
}
