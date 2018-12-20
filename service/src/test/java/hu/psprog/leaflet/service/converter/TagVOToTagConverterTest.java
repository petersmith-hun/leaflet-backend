package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TagVOToTagConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TagVOToTagConverterTest extends ConversionTestObjects {

    @InjectMocks
    private TagVOToTagConverter converter;

    @Test
    public void shouldConvert() {

        // when
        Tag result = converter.convert(TAG_VO);

        // then
        assertThat(result, equalTo(TAG));
    }
}