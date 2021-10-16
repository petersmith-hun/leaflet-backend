package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link DocumentUpdateRequestModelToDocumentVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class DocumentUpdateRequestModelToDocumentVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private DocumentUpdateRequestModelToDocumentVOConverter converter;

    @Test
    public void shouldConvertCreateRequest() {

        // when
        DocumentVO result = converter.convert(DOCUMENT_CREATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(DOCUMENT_VO_WITH_OWNER_FOR_CREATE));
    }

    @Test
    public void shouldConvertUpdateRequest() {

        // when
        DocumentVO result = converter.convert(DOCUMENT_UPDATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(DOCUMENT_VO_WITHOUT_OWNER_FOR_UPDATE));
    }
}