package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.MailFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link MailFactory} implementation for password reset success notification mails.
 *
 * @author Peter Smith
 */
@Component
public class PasswordResetSuccessMailFactory implements MailFactory<String> {

    private static final String PASSWORD_RESET_CONFIRM_MAIL_SUBJECT = "Password successfully reset";
    private static final String PASSWORD_RESET_CONFIRM_MAIL_TEMPLATE = "pw_reset_confirm.html";

    private static final String GENERATED_AT = "generatedAt";
    private static final String USERNAME = "username";

    @Override
    public Mail buildMail(String content, String... recipient) {

        Assert.isTrue(Objects.nonNull(recipient) && recipient.length == 1, "Exactly one recipient is required.");

        return Mail.getBuilder()
                .withRecipient(recipient[0])
                .withSubject(PASSWORD_RESET_CONFIRM_MAIL_SUBJECT)
                .withTemplate(PASSWORD_RESET_CONFIRM_MAIL_TEMPLATE)
                .withContentMap(createContentMap(content))
                .build();
    }

    private Map<String, Object> createContentMap(String username) {

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put(USERNAME, username);
        contentMap.put(GENERATED_AT, DATE_FORMAT.format(new Date()));

        return contentMap;
    }
}
