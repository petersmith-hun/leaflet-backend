package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link DocumentVOToEditDocumentDataModelListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class DocumentVOToEditDocumentDataModelListConverterTest extends ConversionTestObjects {

    @Mock
    private DocumentVOToEditDocumentDataModelEntityConverter entityConverter;

    @InjectMocks
    private DocumentVOToEditDocumentDataModelListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(DOCUMENT_VO_WITH_OWNER)).willReturn(EDIT_DOCUMENT_DATA_MODEL);

        // when
        DocumentListDataModel result = converter.convert(Collections.singletonList(DOCUMENT_VO_WITH_OWNER));

        // then
        assertThat(result, equalTo(DOCUMENT_LIST_DATA_MODEL));
    }
}