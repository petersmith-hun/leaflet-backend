package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UploadedFileVOToFileDataModelConverter}
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UploadedFileVOToFileDataModelConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UploadedFileVOToFileDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // when
        FileDataModel result = converter.convert(UPLOADED_FILE_VO);

        // then
        assertThat(result, equalTo(FILE_DATA_MODEL));
    }
}