package hu.psprog.leaflet;

import hu.psprog.leaflet.web.config.EmbeddedWebServerAJPCustomization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * Spring Boot entry point.
 *
 * @author Peter Smith
 */
@SpringBootApplication(exclude = {
        H2ConsoleAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        MailSenderAutoConfiguration.class,
        ThymeleafAutoConfiguration.class})
public class LeafletBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeafletBackendApplication.class, args);
    }

    @Bean
    @Profile("production")
    public EmbeddedWebServerAJPCustomization ajpContainerCustomizer(@Value("${tomcat.ajp.port}") int ajpPort) {
        return new EmbeddedWebServerAJPCustomization(ajpPort);
    }
}
