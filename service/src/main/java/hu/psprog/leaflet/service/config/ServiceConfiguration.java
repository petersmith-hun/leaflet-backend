package hu.psprog.leaflet.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peter Smith
 */
@Configuration
@ComponentScan(ServiceConfiguration.PERSISTENCE_FACADE_PACKAGE)
public class ServiceConfiguration {
    public static final String PERSISTENCE_FACADE_PACKAGE = "hu.psprog.leaflet.persistence.facade";
}
