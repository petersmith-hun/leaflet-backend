package hu.psprog.leaflet.bridge.client.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Utilities for generating tokens during acceptance test execution.
 *
 * @author Peter Smith
 */
@Component
public class AccessTokenTestUtility {

    private static final int EXPIRATION_IN_MINUTES = 10;

    private final RoleToAuthoritiesMapping roleToAuthoritiesMapping;
    private final UserDAO userDAO;
    private final JWSSigner jwsSigner;
    private final JWSHeader jwsHeader;
    private final String defaultUser;

    @Autowired
    public AccessTokenTestUtility(RoleToAuthoritiesMapping roleToAuthoritiesMapping, UserDAO userDAO, String jwtSecret,
                                  @Value("${test-auth.default-user}") String defaultUser) throws KeyLengthException {
        this.roleToAuthoritiesMapping = roleToAuthoritiesMapping;
        this.userDAO = userDAO;
        this.defaultUser = defaultUser;
        this.jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        this.jwsSigner = new MACSigner(jwtSecret);
    }

    /**
     * Generates a JWT token for the default acceptance test user.
     *
     * @return generated JWT token as {@link String}
     */
    public String generateToken() {
        return generateToken(defaultUser);
    }

    /**
     * Generates a JWT token for the given acceptance test user.
     *
     * @param username email address of the user to be authorized
     * @return generated JWT token as {@link String}
     */
    public String generateToken(String username) {

        User user = userDAO.findByEmail(username);
        String scope = roleToAuthoritiesMapping.getAuthoritiesForRole(user.getRole());

        Payload payload = new Payload(Map.of(
                "scope", scope,
                "name", user.getUsername(),
                "usr", user.getEmail(),
                "uid", user.getId(),
                "exp", generateExpiration()
        ));

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(jwsSigner);
        } catch (JOSEException e) {
            fail("Failed to sign JWT access token");
        }

        return jwsObject.serialize();
    }

    private long generateExpiration() {

        return LocalDateTime.now()
                .plusMinutes(EXPIRATION_IN_MINUTES)
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
    }
}
