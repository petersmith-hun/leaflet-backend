package hu.psprog.leaflet.web.config;

import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.web.filter.RequestContextFilter;

@Configuration
@EnableAspectJAutoProxy
public class ApplicationContextConfig {

    @Bean
    public RequestContextFilter requestContextFilter() {
        OrderedRequestContextFilter orderedRequestContextFilter = new OrderedRequestContextFilter();
        orderedRequestContextFilter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return orderedRequestContextFilter;
    }
}
