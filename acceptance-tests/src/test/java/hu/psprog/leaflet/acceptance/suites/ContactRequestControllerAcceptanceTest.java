package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.ContactBridgeService;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance test for {@code /contact} endpoint.
 *
 * @author Peter Smith
 */
@LeafletAcceptanceSuite
public class ContactRequestControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final ContactRequestModel CONTACT_REQUEST_MODEL = new ContactRequestModel();
    private static final String NAME = "name";
    private static final String EMAIL = "test@local.dev";
    private static final String MESSAGE = "contact-message";
    private static final ContactRequestVO CONTACT_REQUEST_VO = ContactRequestVO.getBuilder()
            .withName(NAME)
            .withEmail(EMAIL)
            .withMessage(MESSAGE)
            .build();

    static {
        CONTACT_REQUEST_MODEL.setName(NAME);
        CONTACT_REQUEST_MODEL.setEmail(EMAIL);
        CONTACT_REQUEST_MODEL.setMessage(MESSAGE);
    }

    @Autowired
    private ContactBridgeService contactBridgeService;

    @Test
    public void shouldProcessContactRequest() throws CommunicationFailureException {

        // when
        contactBridgeService.sendContactRequest(CONTACT_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // then
        assertThat(notificationService.getContactRequestVO(), equalTo(CONTACT_REQUEST_VO));
    }
}
