package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
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
 * Unit tests for {@link UploadedFileVOListToFileListDataModelConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UploadedFileVOListToFileListDataModelConverterTest extends ConversionTestObjects {

    @Mock
    private UploadedFileVOToFileDataModelConverter entityConverter;

    @InjectMocks
    private UploadedFileVOListToFileListDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(UPLOADED_FILE_VO)).willReturn(FILE_DATA_MODEL);

        // then
        FileListDataModel result = converter.convert(Collections.singletonList(UPLOADED_FILE_VO));

        // then
        assertThat(result, equalTo(FILE_LIST_DATA_MODEL));
    }
}