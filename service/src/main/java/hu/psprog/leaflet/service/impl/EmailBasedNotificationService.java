package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.mail.client.MailClient;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.mail.impl.MailFactoryRegistry;
import hu.psprog.leaflet.service.mail.impl.PasswordResetRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.PasswordResetSuccessMailFactory;
import hu.psprog.leaflet.service.mail.impl.SystemStartupMailFactory;
import hu.psprog.leaflet.service.observer.impl.LoggingMailObserverHandler;
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
        loggingMailObserverHandler.attachObserver(mailClient.sendMail(mail));
    }

    @Override
    public void passwordResetRequested(PasswordResetRequest passwordResetRequest) {
        Mail mail = mailFactoryRegistry
                .getFactory(PasswordResetRequestMailFactory.class)
                .buildMail(passwordResetRequest, passwordResetRequest.getParticipant());
        loggingMailObserverHandler.attachObserver(mailClient.sendMail(mail));
    }

    @Override
    public void successfulPasswordReset(PasswordResetSuccess passwordResetSuccess) {
        Mail mail = mailFactoryRegistry
                .getFactory(PasswordResetSuccessMailFactory.class)
                .buildMail(passwordResetSuccess.getUsername(), passwordResetSuccess.getParticipant());
        loggingMailObserverHandler.attachObserver(mailClient.sendMail(mail));
    }
}
