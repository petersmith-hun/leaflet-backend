package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.mail.client.MailClient;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.mail.domain.SignUpConfirmation;
import hu.psprog.leaflet.service.mail.impl.CommentNotificationMailFactory;
import hu.psprog.leaflet.service.mail.impl.ContactRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.MailFactoryRegistry;
import hu.psprog.leaflet.service.mail.impl.PasswordResetRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.PasswordResetSuccessMailFactory;
import hu.psprog.leaflet.service.mail.impl.SignUpConfirmationMailFactory;
import hu.psprog.leaflet.service.mail.impl.SystemStartupMailFactory;
import hu.psprog.leaflet.service.observer.impl.LoggingMailObserverHandler;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Email-based implementation of {@link NotificationService}.
 *
 * @author Peter Smith
 */
@Service
public class EmailBasedNotificationService implements NotificationService {

    private MailClient mailClient;
    private LoggingMailObserverHandler loggingMailObserverHandler;
    private MailFactoryRegistry mailFactoryRegistry;

    @Autowired
    public EmailBasedNotificationService(MailClient mailClient, LoggingMailObserverHandler loggingMailObserverHandler, MailFactoryRegistry mailFactoryRegistry) {
        this.mailClient = mailClient;
        this.loggingMailObserverHandler = loggingMailObserverHandler;
        this.mailFactoryRegistry = mailFactoryRegistry;
    }

    @Override
    public void startupFinished(String version) {
        Mail mail = mailFactoryRegistry
                .getFactory(SystemStartupMailFactory.class)
                .buildMail(version);
        sendMailAndAttachObserver(mail);
    }

    @Override
    public void passwordResetRequested(PasswordResetRequest passwordResetRequest) {
        Mail mail = mailFactoryRegistry
                .getFactory(PasswordResetRequestMailFactory.class)
                .buildMail(passwordResetRequest, passwordResetRequest.getParticipant());
        sendMailAndAttachObserver(mail);
    }

    @Override
    public void successfulPasswordReset(PasswordResetSuccess passwordResetSuccess) {
        Mail mail = mailFactoryRegistry
                .getFactory(PasswordResetSuccessMailFactory.class)
                .buildMail(passwordResetSuccess.getUsername(), passwordResetSuccess.getParticipant());
        sendMailAndAttachObserver(mail);
    }

    @Override
    public void commentNotification(CommentNotification commentNotification) {
        Mail mail = mailFactoryRegistry
                .getFactory(CommentNotificationMailFactory.class)
                .buildMail(commentNotification, commentNotification.getAuthorEmail());
        sendMailAndAttachObserver(mail);
    }

    @Override
    public void contactRequestReceived(ContactRequestVO contactRequestVO) {
        Mail mail = mailFactoryRegistry
                .getFactory(ContactRequestMailFactory.class)
                .buildMail(contactRequestVO);
        sendMailAndAttachObserver(mail);
    }

    @Override
    public void signUpConfirmation(SignUpConfirmation signUpConfirmation) {
        Mail mail = mailFactoryRegistry
                .getFactory(SignUpConfirmationMailFactory.class)
                .buildMail(signUpConfirmation, signUpConfirmation.getEmail());
        sendMailAndAttachObserver(mail);
    }

    private void sendMailAndAttachObserver(Mail mail) {
        loggingMailObserverHandler.attachObserver(mailClient.sendMail(mail));
    }
}
