package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link DocumentVOToDocumentDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentVOToDocumentDataModelEntityConverterTest extends ConversionTestObjects {

    @InjectMocks
    private DocumentVOToDocumentDataModelEntityConverter converter;

    @Test
    public void shouldConvert() {

        // when
        DocumentDataModel result = converter.convert(DOCUMENT_VO_WITH_OWNER);

        // then
        assertThat(result, equalTo(DOCUMENT_DATA_MODEL));
    }
}