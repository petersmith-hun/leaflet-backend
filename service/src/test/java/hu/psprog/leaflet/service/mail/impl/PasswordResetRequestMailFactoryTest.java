package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link PasswordResetRequestMailFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordResetRequestMailFactoryTest {

    private static final String SUBJECT = "Password reset requested";
    private static final String TEMPLATE = "pw_reset_request.html";
    private static final String RECIPIENT = "test@local.dev";

    private static final String GENERATED_AT = "generatedAt";
    private static final String EXPIRATION = "expiration";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String RESET_LINK = "resetLink";

    private static final String ELEVATED_RESET_URL = "elevated-reset-url";
    private static final String VISITOR_RESET_URL = "visitor-reset-url";

    @InjectMocks
    private PasswordResetRequestMailFactory passwordResetRequestMailFactory;

    @Before
    public void setup() {
        passwordResetRequestMailFactory.setElevated(ELEVATED_RESET_URL);
        passwordResetRequestMailFactory.setVisitor(VISITOR_RESET_URL);
    }

    @Test
    public void shouldBuildMailWithElevatedUser() {

        // given
        PasswordResetRequest passwordResetRequest = preparePasswordResetRequest(true);

        // when
        Mail result = passwordResetRequestMailFactory.buildMail(passwordResetRequest, RECIPIENT);

        // then
        assertGeneratedMail(result, passwordResetRequest, true);
    }

    @Test
    public void shouldBuildMailWithVisitorUser() {

        // given
        PasswordResetRequest passwordResetRequest = preparePasswordResetRequest(false);

        // when
        Mail result = passwordResetRequestMailFactory.buildMail(passwordResetRequest, RECIPIENT);

        // then
        assertGeneratedMail(result, passwordResetRequest, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullRecipient() {

        // given
        passwordResetRequestMailFactory.buildMail(PasswordResetRequest.getBuilder().build());

        // then
        // exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnMultipleRecipients() {

        // given
        passwordResetRequestMailFactory.buildMail(PasswordResetRequest.getBuilder().build(), RECIPIENT, RECIPIENT);

        // then
        // exception expected
    }

    private void assertGeneratedMail(Mail result, PasswordResetRequest passwordResetRequest, boolean withElevatedResetURL) {
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), equalTo(RECIPIENT));
        assertThat(result.getSubject(), equalTo(SUBJECT));
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