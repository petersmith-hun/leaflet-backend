package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TagCreateRequestModelToTagVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TagCreateRequestModelToTagVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private TagCreateRequestModelToTagVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        TagVO result = converter.convert(TAG_CREATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(TAG_VO_FOR_CREATE));
    }
}