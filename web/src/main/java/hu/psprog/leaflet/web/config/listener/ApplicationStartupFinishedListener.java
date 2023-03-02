package hu.psprog.leaflet.web.config.listener;

import hu.psprog.leaflet.service.impl.EmailBasedNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Application startup finished listener.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationStartupFinishedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupFinishedListener.class);
    private static final String STARTUP_EVENT_EMAIL_ENABLED = "${mail.event.startup.enabled:true}";
    private static final String UNKNOWN_BUILD_TIME = "unknown";

    private final EmailBasedNotificationService emailBasedNotificationService;
    private final boolean startupEventEmailEnabled;
    private final Optional<BuildProperties> optionalBuildProperties;

    @Autowired
    public ApplicationStartupFinishedListener(EmailBasedNotificationService emailBasedNotificationService,
                                              @Value(STARTUP_EVENT_EMAIL_ENABLED) boolean startupEventEmailEnabled,
                                              @Autowired(required = false) Optional<BuildProperties> optionalBuildProperties) {
        this.emailBasedNotificationService = emailBasedNotificationService;
        this.startupEventEmailEnabled = startupEventEmailEnabled;
        this.optionalBuildProperties = optionalBuildProperties;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        optionalBuildProperties.ifPresent(buildProperties -> {
            LOGGER.info("Application loaded successfully, running version v{}, built on {}", buildProperties.getVersion(), getBuildTime(buildProperties));
            if (startupEventEmailEnabled) {
                emailBasedNotificationService.startupFinished(buildProperties.getVersion());
            }
        });
    }

    private String getBuildTime(BuildProperties buildProperties) {

        return Optional.ofNullable(buildProperties.getTime())
                .map(buildTime -> buildTime.atZone(ZoneId.systemDefault()))
                .map(ZonedDateTime::toString)
                .orElse(UNKNOWN_BUILD_TIME);
    }
}
