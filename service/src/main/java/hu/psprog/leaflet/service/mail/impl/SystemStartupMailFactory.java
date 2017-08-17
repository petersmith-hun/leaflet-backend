package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.MailFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link MailFactory} implementation for system startup notification mails.
 *
 * @author Peter Smith
 */
@Component
public class SystemStartupMailFactory implements MailFactory<String> {

    private static final String SYSTEM_STARTUP_MAIL_SUBJECT = "Leaflet started up";
    private static final String SYSTEM_STARTUP_MAIL_TEMPLATE = "system_startup.html";

    private static final String VERSION = "version";
    private static final String GENERATED_AT = "generatedAt";

    @Override
    public Mail buildMail(String version, String... recipient) {

        return Mail.getBuilder()
                .withSubject(SYSTEM_STARTUP_MAIL_SUBJECT)
                .withTemplate(SYSTEM_STARTUP_MAIL_TEMPLATE)
                .withContentMap(createContentMap(version))
                .build();
    }

    private Map<String, Object> createContentMap(String version) {

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put(VERSION, version);
        contentMap.put(GENERATED_AT, DATE_FORMAT.format(new Date()));

        return contentMap;
    }
}
