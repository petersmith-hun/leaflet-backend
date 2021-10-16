package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UploadedFileVOToUploadedFileConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UploadedFileVOToUploadedFileConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UploadedFileVOToUploadedFileConverter converter;

    @Test
    public void shouldConvert() {

        // when
        UploadedFile result = converter.convert(UPLOADED_FILE_VO);

        // then
        assertThat(result, equalTo(UPLOADED_FILE));
    }
}