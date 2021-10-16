package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ContactRequestMailFactory}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ContactRequestMailFactoryTest {

    private static final String SUBJECT = "mail.user.notification.contact.subject";
    private static final String TRANSLATED_SUBJECT = "Contact request";
    private static final String TEMPLATE = "contact_request.html";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MESSAGE = "message";
    private static final String GENERATED_AT = "generatedAt";
    private static final Locale FORCED_LOCALE = Locale.ENGLISH;

    private static final ContactRequestVO CONTACT_REQUEST_VO = ContactRequestVO.getBuilder()
            .withName("name")
            .withEmail("test@local.dev")
            .withMessage("contact message")
            .build();

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ContactRequestMailFactory contactRequestMailFactory;

    @Test
    public void shouldBuildMail() {

        // given
        contactRequestMailFactory.setForcedLocale(FORCED_LOCALE);
        given(messageSource.getMessage(SUBJECT, null, FORCED_LOCALE)).willReturn(TRANSLATED_SUBJECT);

        // when
        Mail result = contactRequestMailFactory.buildMail(CONTACT_REQUEST_VO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), nullValue());
        assertThat(result.getSubject(), equalTo(TRANSLATED_SUBJECT));
        assertThat(result.getTemplate(), equalTo(TEMPLATE));
        assertThat(result.getReplyTo(), equalTo(CONTACT_REQUEST_VO.getEmail()));
        assertThat(result.getContentMap().get(NAME), equalTo(CONTACT_REQUEST_VO.getName()));
        assertThat(result.getContentMap().get(EMAIL), equalTo(CONTACT_REQUEST_VO.getEmail()));
        assertThat(result.getContentMap().get(MESSAGE), equalTo(CONTACT_REQUEST_VO.getMessage()));
        assertThat(result.getContentMap().get(GENERATED_AT), notNullValue());
    }
}
