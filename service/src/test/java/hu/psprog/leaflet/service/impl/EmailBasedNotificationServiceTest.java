package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.lens.api.domain.ContactRequest;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.api.domain.SystemStartup;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import hu.psprog.leaflet.service.vo.mail.CommentNotification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EmailBasedNotificationService}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EmailBasedNotificationServiceTest {

    private static final String DESTINATION_EMAIL_ADDRESS = "destination-email-address";
    private static final String VERSION = "v1";
    private static final String USERNAME = "User 1";
    private static final String EMAIL = "user1@dev.local";
    private static final String CONTENT = "This is the content";
    private static final String AUTHOR_NAME = "Editor 1";
    private static final String ENTRY_TITLE = "Article title";

    @Mock
    private EventNotificationServiceClient eventNotificationServiceClient;

    @InjectMocks
    private EmailBasedNotificationService emailBasedNotificationService;

    @Test
    public void shouldSendStartupFinishedNotification() throws CommunicationFailureException {

        // given
        MailRequestWrapper<SystemStartup> systemStartupMail = MailRequestWrapper.<SystemStartup>builder()
                .overrideSubjectKey("mail.system.event.startup.subject.leaflet")
                .content(SystemStartup.builder()
                        .applicationName("Leaflet Backend")
                        .version(VERSION)
                        .build())
                .build();

        // when
        emailBasedNotificationService.startupFinished(VERSION);

        // then
        verify(eventNotificationServiceClient).requestMailNotification(systemStartupMail);
    }

    @Test
    public void shouldAnyOperationFailSilentlyIfCommunicationFailureExceptionIsThrown() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(eventNotificationServiceClient).requestMailNotification(any(MailRequestWrapper.class));

        // when
        emailBasedNotificationService.startupFinished(VERSION);

        // then
        // silent failure expected
    }

    @Test
    public void shouldSendCommentNotification() throws CommunicationFailureException {

        // given
        CommentNotification commentNotification = CommentNotification.getBuilder()
                .withUsername(USERNAME)
                .withEmail(EMAIL)
                .withContent(CONTENT)
                .withAuthorName(AUTHOR_NAME)
                .withAuthorEmail(DESTINATION_EMAIL_ADDRESS)
                .withEntryTitle(ENTRY_TITLE)
                .build();
        MailRequestWrapper<hu.psprog.leaflet.lens.api.domain.CommentNotification> commentNotificationMail = MailRequestWrapper.<hu.psprog.leaflet.lens.api.domain.CommentNotification>builder()
                .recipients(DESTINATION_EMAIL_ADDRESS)
                .content(hu.psprog.leaflet.lens.api.domain.CommentNotification.builder()
                        .username(USERNAME)
                        .email(EMAIL)
                        .content(CONTENT)
                        .authorName(AUTHOR_NAME)
                        .authorEmail(DESTINATION_EMAIL_ADDRESS)
                        .entryTitle(ENTRY_TITLE)
                        .build())
                .build();

        // when
        emailBasedNotificationService.commentNotification(commentNotification);

        // then
        verify(eventNotificationServiceClient).requestMailNotification(commentNotificationMail);
    }

    @Test
    public void shouldSendContactRequestNotification() throws CommunicationFailureException {

        // given
        ContactRequestVO contactRequestVO = ContactRequestVO.getBuilder()
                .withName(USERNAME)
                .withMessage(CONTENT)
                .withEmail(EMAIL)
                .build();
        MailRequestWrapper<ContactRequest> contactRequestMail = MailRequestWrapper.<ContactRequest>builder()
                .replyTo(EMAIL)
                .content(ContactRequest.builder()
                        .name(USERNAME)
                        .email(EMAIL)
                        .message(CONTENT)
                        .build())
                .build();

        // when
        emailBasedNotificationService.contactRequestReceived(contactRequestVO);

        // then
        verify(eventNotificationServiceClient).requestMailNotification(contactRequestMail);
    }
}