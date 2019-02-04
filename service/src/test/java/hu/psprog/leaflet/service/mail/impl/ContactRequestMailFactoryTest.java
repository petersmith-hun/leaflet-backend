package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ContactRequestMailFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactRequestMailFactoryTest {

    private static final String SUBJECT = "Contact request";
    private static final String TEMPLATE = "contact_request.html";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MESSAGE = "message";
    private static final String GENERATED_AT = "generatedAt";

    private static final ContactRequestVO CONTACT_REQUEST_VO = ContactRequestVO.getBuilder()
            .withName("name")
            .withEmail("test@local.dev")
            .withMessage("contact message")
            .build();

    @InjectMocks
    private ContactRequestMailFactory contactRequestMailFactory;

    @Test
    public void shouldBuildMail() {

        // when
        Mail result = contactRequestMailFactory.buildMail(CONTACT_REQUEST_VO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), nullValue());
        assertThat(result.getSubject(), equalTo(SUBJECT));
        assertThat(result.getTemplate(), equalTo(TEMPLATE));
        assertThat(result.getReplyTo(), equalTo(CONTACT_REQUEST_VO.getEmail()));
        assertThat(result.getContentMap().get(NAME), equalTo(CONTACT_REQUEST_VO.getName()));
        assertThat(result.getContentMap().get(EMAIL), equalTo(CONTACT_REQUEST_VO.getEmail()));
        assertThat(result.getContentMap().get(MESSAGE), equalTo(CONTACT_REQUEST_VO.getMessage()));
        assertThat(result.getContentMap().get(GENERATED_AT), notNullValue());
    }
}
