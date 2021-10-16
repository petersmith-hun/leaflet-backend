package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link MailingContactServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class MailingContactServiceImplTest {

    private static final ContactRequestVO CONTACT_REQUEST_VO = ContactRequestVO.getBuilder().build();

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MailingContactServiceImpl mailingContactService;

    @Test
    public void shouldProcessContactRequest() {

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService).contactRequestReceived(CONTACT_REQUEST_VO);
    }
}
