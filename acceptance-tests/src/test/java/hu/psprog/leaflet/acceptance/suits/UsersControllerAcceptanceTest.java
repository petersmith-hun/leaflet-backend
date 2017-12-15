package hu.psprog.leaflet.acceptance.suits;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuit;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance tests for {@code /users} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(SpringRunner.class)
@LeafletAcceptanceSuit
public class UsersControllerAcceptanceTest {

    @Autowired
    private UserBridgeService userBridgeService;

    @Test
    public void shouldGetAllUsers() throws CommunicationFailureException {

        // when
        UserListDataModel result = userBridgeService.getAllUsers();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getUsers().size(), equalTo(5));
    }
}
