package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link DocumentVOToDocumentConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class DocumentVOToDocumentConverterTest extends ConversionTestObjects {

    @InjectMocks
    private DocumentVOToDocumentConverter converter;

    @Test
    public void shouldConvertWithOwner() {

        // when
        Document result = converter.convert(DOCUMENT_VO_WITH_OWNER);

        // then
        assertThat(result, equalTo(DOCUMENT_WITH_OWNER_MINIMUM));
    }

    @Test
    public void shouldConvertWithoutOwner() {

        // when
        Document result = converter.convert(DOCUMENT_VO_WITHOUT_OWNER);

        // then
        assertThat(result, equalTo(DOCUMENT_WITHOUT_OWNER));
    }

    @Test
    public void shouldConvertMinimum() {

        // when
        Document result = converter.convert(DOCUMENT_MINIMUM_VO);

        // then
        assertThat(result, equalTo(DOCUMENT_MINIMUM));
    }
}