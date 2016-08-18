package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles("it")
public class UserServiceImplIT {

    @Autowired
    private UserService userService;

    @Before
    public void setup() {

    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testLoadByUsername() {

        userService.loadUserByUsername("lflt-it-5101@leaflet.dev");
    }
}
