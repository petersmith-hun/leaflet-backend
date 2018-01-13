package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.service.vo.TagVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TagToTagVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TagToTagVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private TagToTagVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        TagVO result = converter.convert(TAG);

        // then
        assertThat(result, equalTo(TAG_VO));
    }
}