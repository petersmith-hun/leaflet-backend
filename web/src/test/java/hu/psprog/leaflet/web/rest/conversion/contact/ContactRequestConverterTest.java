package hu.psprog.leaflet.web.rest.conversion.contact;

import hu.psprog.leaflet.service.vo.ContactRequestVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ContactRequestConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ContactRequestConverterTest extends ConversionTestObjects {

    @InjectMocks
    private ContactRequestConverter contactRequestConverter;

    @Test
    public void shouldConvert() {

        // when
        ContactRequestVO result = contactRequestConverter.convert(CONTACT_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(CONTACT_REQUEST_VO));
    }
}
