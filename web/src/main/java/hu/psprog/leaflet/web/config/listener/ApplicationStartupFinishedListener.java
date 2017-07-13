package hu.psprog.leaflet.web.config.listener;

import hu.psprog.leaflet.service.impl.EmailBasedNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Application startup finished listener.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationStartupFinishedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupFinishedListener.class);
    private static final String APP_BUILD_DATE_PROPERTY = "${app.built}";

    private EmailBasedNotificationService emailBasedNotificationService;
    private String appVersion;
    private String builtOn;

    @Autowired
    public ApplicationStartupFinishedListener(EmailBasedNotificationService emailBasedNotificationService, String appVersion, @Value(APP_BUILD_DATE_PROPERTY) String builtOn) {
        this.emailBasedNotificationService = emailBasedNotificationService;
        this.appVersion = appVersion;
        this.builtOn = builtOn;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Application loaded successfully, running version v{}, built on {}", appVersion, builtOn);
        emailBasedNotificationService.startupFinished(appVersion);
    }
}
