package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.request.file.FileUploadRequestModel;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link FileUploadRequestModelToFileInputVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FileUploadRequestModelToFileInputVOConverterTest extends ConversionTestObjects {

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileUploadRequestModelToFileInputVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        FileInputVO result = converter.convert(FILE_UPLOAD_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(FILE_INPUT_VO));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionOnIOException() throws IOException {

        // given
        doThrow(IOException.class).when(multipartFile).getInputStream();

        // when
        converter.convert(prepareFailingRequest());

        // then
        // exception expected
    }

    private FileUploadRequestModel prepareFailingRequest() {

        FileUploadRequestModel fileUploadRequestModel = new FileUploadRequestModel();
        fileUploadRequestModel.setInputFile(multipartFile);

        return fileUploadRequestModel;
    }
}