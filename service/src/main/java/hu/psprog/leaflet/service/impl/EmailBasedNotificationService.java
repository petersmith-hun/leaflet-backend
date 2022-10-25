package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.lens.api.domain.ContactRequest;
import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.api.domain.SystemStartup;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.mail.domain.SignUpConfirmation;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        MailRequestWrapper<SystemStartup> systemStartupMail = new MailRequestWrapper<>();
        systemStartupMail.setOverrideSubjectKey(SYSTEM_STARTUP_SUBJECT_KEY);
        systemStartupMail.setContent(SystemStartup.builder()
                .applicationName(APPLICATION_NAME)
                .version(version)
                .build());

        submit(systemStartupMail);
    }

    @Override
    public void passwordResetRequested(PasswordResetRequest passwordResetRequest) {
        throw new UnsupportedOperationException("Functionality is not part of Leaflet any longer");
    }

    @Override
    public void successfulPasswordReset(PasswordResetSuccess passwordResetSuccess) {
        throw new UnsupportedOperationException("Functionality is not part of Leaflet any longer");
    }

    @Override
    public void commentNotification(CommentNotification commentNotification) {

        MailRequestWrapper<hu.psprog.leaflet.lens.api.domain.CommentNotification> commentNotificationMail = new MailRequestWrapper<>();
        commentNotificationMail.setRecipients(List.of(commentNotification.getAuthorEmail()));
        commentNotificationMail.setContent(hu.psprog.leaflet.lens.api.domain.CommentNotification.builder()
                .username(commentNotification.getUsername())
                .email(commentNotification.getEmail())
                .content(commentNotification.getContent())
                .authorName(commentNotification.getAuthorName())
                .authorEmail(commentNotification.getAuthorEmail())
                .entryTitle(commentNotification.getEntryTitle())
                .build());

        submit(commentNotificationMail);
    }

    @Override
    public void contactRequestReceived(ContactRequestVO contactRequestVO) {

        MailRequestWrapper<ContactRequest> contactRequestMail = new MailRequestWrapper<>();
        contactRequestMail.setReplyTo(contactRequestVO.getEmail());
        contactRequestMail.setContent(ContactRequest.builder()
                .name(contactRequestVO.getName())
                .email(contactRequestVO.getEmail())
                .message(contactRequestVO.getMessage())
                .build());

        submit(contactRequestMail);
    }

    @Override
    public void signUpConfirmation(SignUpConfirmation signUpConfirmation) {
        throw new UnsupportedOperationException("Functionality is not part of Leaflet any longer");
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
