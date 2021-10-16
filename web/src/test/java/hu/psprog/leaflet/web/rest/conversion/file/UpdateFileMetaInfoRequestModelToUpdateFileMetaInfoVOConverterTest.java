package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UpdateFileMetaInfoRequestModelToUpdateFileMetaInfoVOConverter}
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UpdateFileMetaInfoRequestModelToUpdateFileMetaInfoVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private UpdateFileMetaInfoRequestModelToUpdateFileMetaInfoVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        UpdateFileMetaInfoVO result = converter.convert(UPDATE_FILE_META_INFO_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(UPDATE_FILE_META_INFO_VO));
    }
}