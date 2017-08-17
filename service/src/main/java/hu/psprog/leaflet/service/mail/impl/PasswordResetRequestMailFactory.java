package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import hu.psprog.leaflet.service.mail.MailFactory;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
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
@ConfigurationProperties(prefix = "mail.event.password-reset", ignoreUnknownFields = false)
public class PasswordResetRequestMailFactory implements MailFactory<PasswordResetRequest> {

    private static final String PASSWORD_RESET_REQUEST_MAIL_SUBJECT = "Password reset requested";
    private static final String PASSWORD_RESET_REQUEST_MAIL_TEMPLATE = "pw_reset_request.html";
    private static final String DEFAULT = "default";

    private static final String GENERATED_AT = "generatedAt";
    private static final String EXPIRATION = "expiration";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String RESET_LINK = "resetLink";

    private Map<String, String> urlByRole;

    @PostConstruct
    public void checkUrlMap() {
        Assert.isTrue(urlByRole.containsKey(DEFAULT), "URL map must contain default URL");
    }

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

    public Map<String, String> getUrlByRole() {
        return urlByRole;
    }

    public void setUrlByRole(Map<String, String> urlByRole) {
        this.urlByRole = urlByRole;
    }

    private Map<String, Object> createContentMap(PasswordResetRequest passwordResetRequest) {

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put(TOKEN, passwordResetRequest.getToken());
        contentMap.put(USERNAME, passwordResetRequest.getUserDetails().getName());
        contentMap.put(EXPIRATION, passwordResetRequest.getExpiration());
        contentMap.put(RESET_LINK, getResetURL(passwordResetRequest.getUserDetails()));
        contentMap.put(GENERATED_AT, DATE_FORMAT.format(new Date()));

        return contentMap;
    }

    private String getResetURL(ExtendedUserDetails userDetails) {
        String authority = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        return urlByRole.getOrDefault(authority, urlByRole.get(DEFAULT));
    }
}
