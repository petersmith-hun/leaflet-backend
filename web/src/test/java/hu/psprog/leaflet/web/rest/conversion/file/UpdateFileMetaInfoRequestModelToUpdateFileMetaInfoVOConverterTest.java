package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link UpdateFileMetaInfoRequestModelToUpdateFileMetaInfoVOConverter}
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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