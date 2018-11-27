package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link DocumentVOToEditDocumentDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentVOToEditDocumentDataModelEntityConverterTest extends ConversionTestObjects {

    @InjectMocks
    private DocumentVOToEditDocumentDataModelEntityConverter converter;

    @Test
    public void shouldConvert() {

        // when
        EditDocumentDataModel result = converter.convert(DOCUMENT_VO_WITH_OWNER);

        // then
        assertThat(result, equalTo(EDIT_DOCUMENT_DATA_MODEL));
    }
}