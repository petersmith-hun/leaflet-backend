package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.MailFactory;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link MailFactory} implementation for password reset request mails.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "mail.event.password-reset.url", ignoreUnknownFields = false)
public class PasswordResetRequestMailFactory implements MailFactory<PasswordResetRequest> {

    private static final String PASSWORD_RESET_REQUEST_MAIL_SUBJECT = "Password reset requested";
    private static final String PASSWORD_RESET_REQUEST_MAIL_TEMPLATE = "pw_reset_request.html";

    private static final String GENERATED_AT = "generatedAt";
    private static final String EXPIRATION = "expiration";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String RESET_LINK = "resetLink";

    private String elevated;
    private String visitor;

    @Override
    public Mail buildMail(PasswordResetRequest content, String... recipient) {

        Assert.isTrue(Objects.nonNull(recipient) && recipient.length == 1, "Exactly one recipient is required.");

        return Mail.getBuilder()
                .withRecipient(recipient[0])
                .withSubject(PASSWORD_RESET_REQUEST_MAIL_SUBJECT)
                .withTemplate(PASSWORD_RESET_REQUEST_MAIL_TEMPLATE)
                .withContentMap(createContentMap(content))
                .build();
    }

    public String getElevated() {
        return elevated;
    }

    public void setElevated(String elevated) {
        this.elevated = elevated;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    private Map<String, Object> createContentMap(PasswordResetRequest passwordResetRequest) {

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put(TOKEN, passwordResetRequest.getToken());
        contentMap.put(USERNAME, passwordResetRequest.getUsername());
        contentMap.put(EXPIRATION, passwordResetRequest.getExpiration());
        contentMap.put(RESET_LINK, getResetURL(passwordResetRequest));
        contentMap.put(GENERATED_AT, DATE_FORMAT.format(new Date()));

        return contentMap;
    }

    private String getResetURL(PasswordResetRequest passwordResetRequest) {

        String resetURL = visitor;
        if (passwordResetRequest.isElevated()) {
            resetURL = elevated;
        }

        return resetURL;
    }
}
