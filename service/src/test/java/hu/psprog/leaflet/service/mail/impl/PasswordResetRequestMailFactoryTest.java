package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link PasswordResetRequestMailFactory}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class PasswordResetRequestMailFactoryTest {

    private static final String SUBJECT = "mail.user.pwreset.demand.subject";
    private static final String TRANSLATED_SUBJECT = "Password reset requested";
    private static final String TEMPLATE = "pw_reset_request.html";
    private static final String RECIPIENT = "test@local.dev";

    private static final String GENERATED_AT = "generatedAt";
    private static final String EXPIRATION = "expiration";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String RESET_LINK = "resetLink";

    private static final String ELEVATED_RESET_URL = "elevated-reset-url";
    private static final String VISITOR_RESET_URL = "visitor-reset-url";
    private static final Locale FORCED_LOCALE = Locale.ENGLISH;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PasswordResetRequestMailFactory passwordResetRequestMailFactory;

    @BeforeEach
    public void setup() {
        passwordResetRequestMailFactory.setElevated(ELEVATED_RESET_URL);
        passwordResetRequestMailFactory.setVisitor(VISITOR_RESET_URL);
        passwordResetRequestMailFactory.setForcedLocale(FORCED_LOCALE);
    }

    @Test
    public void shouldBuildMailWithElevatedUser() {

        // given
        PasswordResetRequest passwordResetRequest = preparePasswordResetRequest(true);
        given(messageSource.getMessage(SUBJECT, null, FORCED_LOCALE)).willReturn(TRANSLATED_SUBJECT);

        // when
        Mail result = passwordResetRequestMailFactory.buildMail(passwordResetRequest, RECIPIENT);

        // then
        assertGeneratedMail(result, passwordResetRequest, true);
    }

    @Test
    public void shouldBuildMailWithVisitorUser() {

        // given
        PasswordResetRequest passwordResetRequest = preparePasswordResetRequest(false);
        given(messageSource.getMessage(SUBJECT, null, FORCED_LOCALE)).willReturn(TRANSLATED_SUBJECT);

        // when
        Mail result = passwordResetRequestMailFactory.buildMail(passwordResetRequest, RECIPIENT);

        // then
        assertGeneratedMail(result, passwordResetRequest, false);
    }

    @Test
    public void shouldThrowExceptionOnNullRecipient() {

        // given
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordResetRequestMailFactory.buildMail(PasswordResetRequest.getBuilder().build()));

        // then
        // exception expected
    }

    @Test
    public void shouldThrowExceptionOnMultipleRecipients() {

        // given
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordResetRequestMailFactory.buildMail(PasswordResetRequest.getBuilder().build(), RECIPIENT, RECIPIENT));

        // then
        // exception expected
    }

    private void assertGeneratedMail(Mail result, PasswordResetRequest passwordResetRequest, boolean withElevatedResetURL) {
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), equalTo(RECIPIENT));
        assertThat(result.getSubject(), equalTo(TRANSLATED_SUBJECT));
        assertThat(result.getTemplate(), equalTo(TEMPLATE));
        assertThat(result.getContentMap().get(TOKEN), equalTo(passwordResetRequest.getToken()));
        assertThat(result.getContentMap().get(USERNAME), equalTo(passwordResetRequest.getUsername()));
        assertThat(result.getContentMap().get(EXPIRATION), equalTo(passwordResetRequest.getExpiration()));
        assertThat(result.getContentMap().get(RESET_LINK), equalTo(withElevatedResetURL ? ELEVATED_RESET_URL : VISITOR_RESET_URL));
        assertThat(result.getContentMap().get(GENERATED_AT), notNullValue());
    }

    private PasswordResetRequest preparePasswordResetRequest(boolean elevated) {
        return PasswordResetRequest.getBuilder()
                .withToken("token")
                .withExpiration(1)
                .withElevated(elevated)
                .withUsername("username")
                .build();
    }
}