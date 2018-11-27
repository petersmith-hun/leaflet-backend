package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UploadedFileVOToFileDataModelConverter}
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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