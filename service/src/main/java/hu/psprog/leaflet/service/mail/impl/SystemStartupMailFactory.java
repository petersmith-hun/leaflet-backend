package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.MailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class SystemStartupMailFactory extends AbstractMailFactory<String> {

    private static final String SYSTEM_STARTUP_MAIL_SUBJECT = "mail.system.event.startup.subject";
    private static final String SYSTEM_STARTUP_MAIL_TEMPLATE = "system_startup.html";

    private static final String VERSION = "version";
    private static final String GENERATED_AT = "generatedAt";

    @Autowired
    public SystemStartupMailFactory(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Mail buildMail(String version, String... recipient) {

        return Mail.getBuilder()
                .withSubject(translateSubject(SYSTEM_STARTUP_MAIL_SUBJECT))
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
