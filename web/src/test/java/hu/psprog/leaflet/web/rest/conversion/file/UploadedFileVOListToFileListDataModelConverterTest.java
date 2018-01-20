package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link UploadedFileVOListToFileListDataModelConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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