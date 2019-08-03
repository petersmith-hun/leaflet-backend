package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CommentNotificationMailFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentNotificationMailFactoryTest {

    private static final String SUBJECT = "mail.user.notification.comment.subject";
    private static final String TRANSLATED_SUBJECT = "A new comment has been added to your article";
    private static final String TEMPLATE = "comment_notification.html";
    private static final String RECIPIENT = "test@local.dev";

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CONTENT = "content";
    private static final String ENTRY_TITLE = "entryTitle";
    private static final String AUTHOR_NAME = "authorName";
    private static final String GENERATED_AT = "generatedAt";
    private static final Locale FORCED_LOCALE = Locale.ENGLISH;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CommentNotificationMailFactory commentNotificationMailFactory;

    @Test
    public void shouldBuildMail() {

        // given
        commentNotificationMailFactory.setForcedLocale(FORCED_LOCALE);
        given(messageSource.getMessage(SUBJECT, null, FORCED_LOCALE)).willReturn(TRANSLATED_SUBJECT);
        CommentNotification commentNotification = CommentNotification.getBuilder()
                .withAuthorEmail(RECIPIENT)
                .withAuthorName("author name")
                .withEntryTitle("entry title")
                .withContent("content")
                .withEmail("comment-by@local.dev")
                .withUsername("commenter name")
                .build();

        // when
        Mail result = commentNotificationMailFactory.buildMail(commentNotification, RECIPIENT);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), equalTo(RECIPIENT));
        assertThat(result.getSubject(), equalTo(TRANSLATED_SUBJECT));
        assertThat(result.getTemplate(), equalTo(TEMPLATE));
        assertThat(result.getContentMap().get(USERNAME), equalTo(commentNotification.getUsername()));
        assertThat(result.getContentMap().get(EMAIL), equalTo(commentNotification.getEmail()));
        assertThat(result.getContentMap().get(CONTENT), equalTo(commentNotification.getContent()));
        assertThat(result.getContentMap().get(ENTRY_TITLE), equalTo(commentNotification.getEntryTitle()));
        assertThat(result.getContentMap().get(AUTHOR_NAME), equalTo(commentNotification.getAuthorName()));
        assertThat(result.getContentMap().get(GENERATED_AT), notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullRecipient() {

        // given
        commentNotificationMailFactory.buildMail(CommentNotification.getBuilder().build());

        // then
        // exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnMultipleRecipients() {

        // given
        commentNotificationMailFactory.buildMail(CommentNotification.getBuilder().build(), RECIPIENT, RECIPIENT);

        // then
        // exception expected
    }
}