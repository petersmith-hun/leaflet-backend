package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.mail.client.MailClient;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.mail.domain.MailDeliveryInfo;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.observer.impl.LoggingMailObserverHandler;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Email-based implementation of {@link NotificationService}.
 *
 * @author Peter Smith
 */
@Service
public class EmailBasedNotificationService implements NotificationService {

    private static final String VERSION = "version";
    private static final String GENERATED_AT = "generatedAt";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z";

    private static final String SYSTEM_STARTUP_MAIL_SUBJECT = "Leaflet started up";
    private static final String SYSTEM_STARTUP_MAIL_TEMPLATE = "system_startup.html";

    private MailClient mailClient;
    private LoggingMailObserverHandler loggingMailObserverHandler;
    private DateFormat dateFormat;

    @Autowired
    public EmailBasedNotificationService(MailClient mailClient, LoggingMailObserverHandler loggingMailObserverHandler) {
        this.mailClient = mailClient;
        this.loggingMailObserverHandler = loggingMailObserverHandler;
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    @Override
    public void startupFinished(String version) {
        Observable<MailDeliveryInfo> status = mailClient.sendMail(createMail(version));
        loggingMailObserverHandler.attachObserver(status);
    }

    private Mail createMail(String version) {

        return Mail.getBuilder()
                .withSubject(SYSTEM_STARTUP_MAIL_SUBJECT)
                .withTemplate(SYSTEM_STARTUP_MAIL_TEMPLATE)
                .withContentMap(createContentMap(version))
                .build();
    }

    private Map<String, Object> createContentMap(String version) {

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put(VERSION, version);
        contentMap.put(GENERATED_AT, dateFormat.format(new Date()));

        return contentMap;
    }
}
