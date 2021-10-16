package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration tests for {@link UserDetailsServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class UserDetailsServiceImplIT {

    private static final String USER_ID1_EMAIL = "lflt-it-5101@leaflet.dev";

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testLoadByUsernameWithExistingUser() {

        // when
        UserDetails result = userDetailsService.loadUserByUsername(USER_ID1_EMAIL);

        // then
        assertThat(result.getUsername(), equalTo(USER_ID1_EMAIL));
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testLoadByUsernameWithNonExistingUser() {

        // given
        String email = "nonexisting@user.dev";

        // when
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));

        // then
        // expected exception
    }
}
