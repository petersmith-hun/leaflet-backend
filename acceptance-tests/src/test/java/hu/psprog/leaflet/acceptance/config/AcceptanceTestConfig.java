package hu.psprog.leaflet.acceptance.config;

import hu.psprog.leaflet.bridge.client.impl.InvocationFactoryConfig;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Acceptance test mock context configuration.
 *
 * @author Peter Smith
 */
@TestConfiguration
@Import(InvocationFactoryConfig.class)
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.security",
        "hu.psprog.leaflet.bridge"})
@Profile("acceptance")
public class AcceptanceTestConfig {

    private static final String INIT_SCRIPT = "classpath:jwt_session_store_init.sql";
    private static final String DATABASE_NAME = "acceptance-session-store";

    @Bean
    public HttpServletResponse httpServletResponse() {
        return Mockito.mock(HttpServletResponse.class);
    }

    @Bean
    public NamedParameterJdbcTemplate sessionStoreJDBCTemplate() {
        return new NamedParameterJdbcTemplate(sessionStoreDataSource());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    private DataSource sessionStoreDataSource() {

        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName(DATABASE_NAME)
                .addScript(INIT_SCRIPT)
                .build();
    }
}
