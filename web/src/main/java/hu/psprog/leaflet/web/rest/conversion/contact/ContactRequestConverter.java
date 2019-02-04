package hu.psprog.leaflet.web.rest.conversion.contact;

import hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link ContactRequestModel} to {@link ContactRequestVO}.
 *
 * @author Peter Smith
 */
@Component
public class ContactRequestConverter implements Converter<ContactRequestModel, ContactRequestVO> {

    @Override
    public ContactRequestVO convert(ContactRequestModel source) {
        return ContactRequestVO.getBuilder()
                .withName(source.getName())
                .withEmail(source.getEmail())
                .withMessage(source.getMessage())
                .build();
    }
}
