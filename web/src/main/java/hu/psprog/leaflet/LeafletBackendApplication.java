package hu.psprog.leaflet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * Spring Boot entry point.
 *
 * @author Peter Smith
 */
@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class, H2ConsoleAutoConfiguration.class})
public class LeafletBackendApplication {

    private static final String AJP_PROTOCOL = "AJP/1.3";

    public static void main(String[] args) {
        SpringApplication.run(LeafletBackendApplication.class, args);
    }

    @Bean
    @Profile("production")
    public EmbeddedServletContainerCustomizer ajpContainerCustomizer(@Value("${tomcat.ajp.port}") int ajpPort) {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) configurableEmbeddedServletContainer;
                tomcat.setProtocol(AJP_PROTOCOL);
                tomcat.setPort(ajpPort);
            }
        };
    }
}
