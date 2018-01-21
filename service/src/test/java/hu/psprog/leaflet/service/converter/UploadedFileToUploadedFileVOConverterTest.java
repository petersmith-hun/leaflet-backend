package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UploadedFileToUploadedFileVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UploadedFileToUploadedFileVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UploadedFileToUploadedFileVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        UploadedFileVO result = converter.convert(UPLOADED_FILE);

        // then
        assertThat(result, equalTo(UPLOADED_FILE_VO));
    }
}