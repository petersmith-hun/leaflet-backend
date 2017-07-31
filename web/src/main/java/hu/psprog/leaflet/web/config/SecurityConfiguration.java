package hu.psprog.leaflet.web.config;

import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationProvider;
import hu.psprog.leaflet.security.jwt.filter.JWTAuthenticationFilter;
import hu.psprog.leaflet.web.rest.handler.RESTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Web Security configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled =  true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String PATH_USERS_CLAIM = "/users/claim";
    private static final String PATH_USERS_REGISTER = "/users/register";
    private static final String PATH_USERS_INIT = "/users/init";
    private static final String INIT_ACCESS_FILTER_EXPRESSION = "@initModeEnabled";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTComponent jwtComponent;

    @Autowired
    private JWTAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider claimAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    AuthenticationEntryPoint restAuthenticationEntryPoint() {

        return new RESTAuthenticationEntryPoint();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
            .authenticationProvider(claimAuthenticationProvider())
            .authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .addFilterBefore(getJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

            .authorizeRequests()
                .antMatchers(PATH_USERS_CLAIM, PATH_USERS_REGISTER)
                    .anonymous()
                .antMatchers(PATH_USERS_INIT)
                    .access(INIT_ACCESS_FILTER_EXPRESSION)
                .anyRequest()
                    .authenticated()
                .and()

            .csrf()
                .disable()

            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

            .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()

            .anonymous()
                .key(JWTAuthenticationFilter.ANONYMOUS_ID);
    }

    private JWTAuthenticationFilter getJwtAuthenticationFilter() throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtComponent);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return jwtAuthenticationFilter;
    }
}
