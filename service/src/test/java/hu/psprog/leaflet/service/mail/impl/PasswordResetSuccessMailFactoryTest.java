package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
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
 * Unit tests for {@link PasswordResetSuccessMailFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordResetSuccessMailFactoryTest {

    private static final String SUBJECT = "mail.user.pwreset.confirm.subject";
    private static final String TRANSLATED_SUBJECT = "Password successfully reset";
    private static final String TEMPLATE = "pw_reset_confirm.html";
    private static final String RECIPIENT = "test@local.dev";

    private static final String GENERATED_AT = "generatedAt";
    private static final String USERNAME = "username";
    private static final Locale FORCED_LOCALE = Locale.ENGLISH;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PasswordResetSuccessMailFactory passwordResetSuccessMailFactory;

    @Test
    public void shouldBuildMail() {

        // given
        passwordResetSuccessMailFactory.setForcedLocale(FORCED_LOCALE);
        given(messageSource.getMessage(SUBJECT, null, FORCED_LOCALE)).willReturn(TRANSLATED_SUBJECT);

        // when
        Mail result = passwordResetSuccessMailFactory.buildMail(USERNAME, RECIPIENT);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), equalTo(RECIPIENT));
        assertThat(result.getSubject(), equalTo(TRANSLATED_SUBJECT));
        assertThat(result.getTemplate(), equalTo(TEMPLATE));
        assertThat(result.getContentMap().get(USERNAME), equalTo(USERNAME));
        assertThat(result.getContentMap().get(GENERATED_AT), notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullRecipient() {

        // given
        passwordResetSuccessMailFactory.buildMail(USERNAME);

        // then
        // exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnMultipleRecipients() {

        // given
        passwordResetSuccessMailFactory.buildMail(USERNAME, RECIPIENT, RECIPIENT);

        // then
        // exception expected
    }
}