package hu.psprog.leaflet.acceptance.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Leaflet acceptance test suit marker annotation.
 * - Starts up embedded web server with defined port (server.port).
 * - Also defines {@link AcceptanceTestConfig} class for base context configuration (used for required overrides).
 * - Sets active profile to {@code acceptance}.
 * - Marks test class as {@link Transactional}.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = AcceptanceTestConfig.class)
@ActiveProfiles(LeafletAcceptanceSuit.PROFILE_ACCEPTANCE)
@Transactional
public @interface LeafletAcceptanceSuit {

    String PROFILE_ACCEPTANCE = "acceptance";
}

