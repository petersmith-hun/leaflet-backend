package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.exception.InvalidBlacklistRule;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link MailingContactServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class MailingContactServiceImplTest {

    private static final ContactRequestVO CONTACT_REQUEST_VO = ContactRequestVO.getBuilder()
            .withName("Test User")
            .withMessage("This is a test message")
            .withEmail("test1@dev.local")
            .build();

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MailingContactServiceImpl mailingContactService;

    @Test
    public void shouldProcessContactRequestWithNoBlacklist() {

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestWithBlacklistButNoMatchingRule() {

        // given
        mailingContactService.setBlacklist(List.of("name equals 'Blocked User'"));

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestRejectRequestWhenBlacklistRuleIsMatchingForNameExactMatch() {

        // given
        mailingContactService.setBlacklist(List.of("name equals 'Test User'"));

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService, never()).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestRejectRequestWhenBlacklistRuleIsMatchingForNameContain() {

        // given
        mailingContactService.setBlacklist(List.of("name contains 'User'"));

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService, never()).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestRejectRequestWhenBlacklistRuleIsMatchingForMessageExactMatch() {

        // given
        mailingContactService.setBlacklist(List.of("message equals 'This is a test message'"));

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService, never()).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestRejectRequestWhenBlacklistRuleIsMatchingForMessageContain() {

        // given
        mailingContactService.setBlacklist(List.of("message contains 'test'"));

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService, never()).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestRejectRequestWhenBlacklistRuleIsMatchingForEmailExactMatch() {

        // given
        mailingContactService.setBlacklist(List.of("email equals 'test1@dev.local'"));

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService, never()).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestRejectRequestWhenBlacklistRuleIsMatchingForEmailContain() {

        // given
        mailingContactService.setBlacklist(List.of("email contains 'dev.'"));

        // when
        mailingContactService.processContactRequest(CONTACT_REQUEST_VO);

        // then
        verify(notificationService, never()).contactRequestReceived(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldSettingUpBlacklistThrowErrorOnInvalidRuleDefinitionScenario1() {

        // when
        assertThrows(InvalidBlacklistRule.class, () -> mailingContactService.setBlacklist(List.of("name similar 'something'")));

        // then
        // exception expected
    }

    @Test
    public void shouldSettingUpBlacklistThrowErrorOnInvalidRuleDefinitionScenario2() {

        // when
        assertThrows(InvalidBlacklistRule.class, () -> mailingContactService.setBlacklist(List.of("name equals")));

        // then
        // exception expected
    }

    @Test
    public void shouldSettingUpBlacklistThrowErrorOnInvalidRuleDefinitionScenario3() {

        // when
        assertThrows(InvalidBlacklistRule.class, () -> mailingContactService.setBlacklist(List.of("name contains something")));

        // then
        // exception expected
    }
}
