package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TagCreateRequestModelToTagVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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