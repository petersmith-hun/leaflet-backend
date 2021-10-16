package hu.psprog.leaflet.web.rest.conversion.attachment;

import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AttachmentRequestConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AttachmentRequestConverterTest extends ConversionTestObjects {

    @InjectMocks
    private AttachmentRequestConverter converter;

    @Test
    public void shouldConvert() {

        // when
        AttachmentRequestVO result = converter.convert(ATTACHMENT_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(ATTACHMENT_REQUEST_VO));
    }
}