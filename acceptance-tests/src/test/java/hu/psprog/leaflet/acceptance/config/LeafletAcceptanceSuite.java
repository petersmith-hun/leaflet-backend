package hu.psprog.leaflet.acceptance.config;

import hu.psprog.leaflet.acceptance.extension.ResetDatabaseExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Leaflet acceptance test suite marker annotation.
 * - Starts up embedded web server with defined port (server.port).
 * - Also defines {@link AcceptanceTestConfig} class for base context configuration (used for required overrides).
 * - Sets active profile to {@code acceptance}.
 * - Disables rollback mechanism as database must be manually reverted.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = AcceptanceTestConfig.class)
@ActiveProfiles(LeafletAcceptanceSuite.PROFILE_ACCEPTANCE)
@Rollback(false)
@ExtendWith(ResetDatabaseExtension.class)
public @interface LeafletAcceptanceSuite {

    String PROFILE_ACCEPTANCE = "acceptance";
}

