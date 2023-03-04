package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.lens.api.domain.CommentNotification;
import hu.psprog.leaflet.lens.api.domain.ContactRequest;
import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.api.domain.SystemStartup;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Email-based implementation of {@link NotificationService}.
 *
 * @author Peter Smith
 */
@Service
public class EmailBasedNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailBasedNotificationService.class);

    private static final String APPLICATION_NAME = "Leaflet Backend";
    private static final String SYSTEM_STARTUP_SUBJECT_KEY = "mail.system.event.startup.subject.leaflet";

    private final EventNotificationServiceClient eventNotificationServiceClient;

    @Autowired
    public EmailBasedNotificationService(EventNotificationServiceClient eventNotificationServiceClient) {
        this.eventNotificationServiceClient = eventNotificationServiceClient;
    }

    @Override
    public void startupFinished(String version) {

        var systemStartupMail = MailRequestWrapper.<SystemStartup>builder()
                .overrideSubjectKey(SYSTEM_STARTUP_SUBJECT_KEY)
                .content(SystemStartup.builder()
                        .applicationName(APPLICATION_NAME)
                        .version(version)
                        .build())
                .build();

        submit(systemStartupMail);
    }

    @Override
    public void commentNotification(hu.psprog.leaflet.service.vo.mail.CommentNotification commentNotification) {

        var commentNotificationMail = MailRequestWrapper.<CommentNotification>builder()
                .recipients(commentNotification.getAuthorEmail())
                .content(CommentNotification.builder()
                        .username(commentNotification.getUsername())
                        .email(commentNotification.getEmail())
                        .content(commentNotification.getContent())
                        .authorName(commentNotification.getAuthorName())
                        .authorEmail(commentNotification.getAuthorEmail())
                        .entryTitle(commentNotification.getEntryTitle())
                        .build())
                .build();

        submit(commentNotificationMail);
    }

    @Override
    public void contactRequestReceived(ContactRequestVO contactRequestVO) {

        var contactRequestMail = MailRequestWrapper.<ContactRequest>builder()
                .replyTo(contactRequestVO.getEmail())
                .content(ContactRequest.builder()
                        .name(contactRequestVO.getName())
                        .email(contactRequestVO.getEmail())
                        .message(contactRequestVO.getMessage())
                        .build())
                .build();

        submit(contactRequestMail);
    }

    private void submit(MailRequestWrapper<? extends MailContent> mailRequestWrapper) {

        try {
            eventNotificationServiceClient.requestMailNotification(mailRequestWrapper);
            LOGGER.info("Submitted mail of type [{}]", mailRequestWrapper.getContent().getMailContentType());
        } catch (CommunicationFailureException exception) {
            LOGGER.error("Failed to submit mail request", exception);
        }
    }
}
