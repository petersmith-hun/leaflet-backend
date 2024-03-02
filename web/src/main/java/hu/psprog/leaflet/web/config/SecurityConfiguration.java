package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.web.rest.handler.RESTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Web Security configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] PUBLIC_GET_ENDPOINTS = {
            "/actuator/info",
            "/categories/public",
            "/comments/entry/*/*",
            "/documents/link/*",
            "/documents/public",
            "/entries/link/*",
            "/entries/page/*",
            "/entries/category/*/page/*",
            "/entries/tag/*/page/*",
            "/entries/content/page/*",
            "/files/*/*",
            "/tags/public",
            "/sitemap.xml"};
    private static final String[] PUBLIC_POST_ENDPOINTS = {
            "/contact",
            "/users/claim",
            "/users/register",
            "/users/reclaim"};

    private final RESTAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public SecurityConfiguration(RESTAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS)
                            .permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS)
                            .permitAll()
                        .anyRequest()
                            .authenticated())

                .cors(Customizer.withDefaults())

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(restAuthenticationEntryPoint))

                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwtConfigurer -> {}))

                .build();
    }
}
