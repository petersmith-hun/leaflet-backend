package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.MailFactory;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link MailFactory} implementation for comment notification emails.
 *
 * @author Peter Smith
 */
@Component
public class CommentNotificationMailFactory extends AbstractMailFactory<CommentNotification> {

    private static final String COMMENT_NOTIFICATION_MAIL_SUBJECT = "mail.user.notification.comment.subject";
    private static final String COMMENT_NOTIFICATION_MAIL_TEMPLATE = "comment_notification.html";

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CONTENT = "content";
    private static final String ENTRY_TITLE = "entryTitle";
    private static final String AUTHOR_NAME = "authorName";
    private static final String GENERATED_AT = "generatedAt";

    @Autowired
    public CommentNotificationMailFactory(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Mail buildMail(CommentNotification content, String... recipient) {

        Assert.isTrue(Objects.nonNull(recipient) && recipient.length == 1, "Exactly one recipient is required.");

        return Mail.getBuilder()
                .withRecipient(recipient[0])
                .withSubject(translateSubject(COMMENT_NOTIFICATION_MAIL_SUBJECT))
                .withTemplate(COMMENT_NOTIFICATION_MAIL_TEMPLATE)
                .withContentMap(createContentMap(content))
                .build();
    }

    private Map<String, Object> createContentMap(CommentNotification content) {

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put(USERNAME, content.getUsername());
        contentMap.put(EMAIL, content.getEmail());
        contentMap.put(CONTENT, content.getContent());
        contentMap.put(ENTRY_TITLE, content.getEntryTitle());
        contentMap.put(AUTHOR_NAME, content.getAuthorName());
        contentMap.put(GENERATED_AT, DATE_FORMAT.format(new Date()));

        return contentMap;
    }
}
