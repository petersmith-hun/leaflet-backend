package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TagVOToTagDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TagVOToTagDataModelEntityConverterTest extends ConversionTestObjects {

    @InjectMocks
    private TagVOToTagDataModelEntityConverter converter;

    @Test
    public void shouldConvert() {

        // when
        TagDataModel result = converter.convert(TAG_VO);

        // then
        assertThat(result, equalTo(TAG_DATA_MODEL));
    }
}