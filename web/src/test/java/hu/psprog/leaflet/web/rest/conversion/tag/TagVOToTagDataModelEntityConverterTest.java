package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link TagVOToTagDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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