package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.service.mail.MailFactory;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link MailFactory} implementation for contact requests.
 *
 * @author Peter Smith
 */
@Component
public class ContactRequestMailFactory implements MailFactory<ContactRequestVO> {

    private static final String CONTACT_REQUEST_MAIL_SUBJECT = "Contact request";
    private static final String CONTACT_REQUEST_MAIL_TEMPLATE = "contact_request.html";

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MESSAGE = "message";
    private static final String GENERATED_AT = "generatedAt";

    @Override
    public Mail buildMail(ContactRequestVO content, String... recipient) {

        return Mail.getBuilder()
                .withSubject(CONTACT_REQUEST_MAIL_SUBJECT)
                .withTemplate(CONTACT_REQUEST_MAIL_TEMPLATE)
                .withReplyTo(content.getEmail())
                .withContentMap(createContentMap(content))
                .build();
    }

    private Map<String, Object> createContentMap(ContactRequestVO contactRequestVO) {

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put(NAME, contactRequestVO.getName());
        contentMap.put(EMAIL, contactRequestVO.getEmail());
        contentMap.put(MESSAGE, contactRequestVO.getMessage());
        contentMap.put(GENERATED_AT, DATE_FORMAT.format(new Date()));

        return contentMap;
    }
}
