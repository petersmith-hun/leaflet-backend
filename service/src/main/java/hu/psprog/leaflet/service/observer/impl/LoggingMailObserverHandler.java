package hu.psprog.leaflet.service.observer.impl;

import hu.psprog.leaflet.mail.config.MailProcessorConfigurationProperties;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.mail.domain.MailDeliveryInfo;
import hu.psprog.leaflet.service.impl.EmailBasedNotificationService;
import hu.psprog.leaflet.service.observer.ObserverHandler;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Logging-only observer for {@link MailDeliveryInfo}-returning observables.
 *
 * @author Peter Smith
 */
@Component
public class LoggingMailObserverHandler implements ObserverHandler<MailDeliveryInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailBasedNotificationService.class);
    private static final String MAIL_DELIVERY_INFO_MESSAGE_PATTERN = "Mail delivery status: [%s] '%s' -> %s (%d constraint violations)";

    private MailProcessorConfigurationProperties mailProcessorConfigurationProperties;

    private Consumer<MailDeliveryInfo> mailDeliveryInfoConsumer = mailDeliveryInfo -> {
        String message = String.format(MAIL_DELIVERY_INFO_MESSAGE_PATTERN,
                mailDeliveryInfo.getMailDeliveryStatus(),
                mailDeliveryInfo.getMail().getSubject(),
                extractRecipient(mailDeliveryInfo.getMail()),
                extractNumberOfConstraintViolations(mailDeliveryInfo.getConstraintViolations()));
        LOGGER.info(message);
    };

    @Autowired
    public LoggingMailObserverHandler(MailProcessorConfigurationProperties mailProcessorConfigurationProperties) {
        this.mailProcessorConfigurationProperties = mailProcessorConfigurationProperties;
    }

    @Override
    public void attachObserver(Observable<MailDeliveryInfo> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(mailDeliveryInfoConsumer);
    }

    private int extractNumberOfConstraintViolations(Map<String, String> constraintViolations) {
        return Optional.ofNullable(constraintViolations)
                .map(Map::size)
                .orElse(0);
    }

    private String extractRecipient(Mail mail) {
        return Optional.ofNullable(mail.getRecipient())
                .orElse(mailProcessorConfigurationProperties.getAdminNotificationAddress());
    }
}
