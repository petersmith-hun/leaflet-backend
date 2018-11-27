package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link TagVOToTagDataModelListConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TagVOToTagDataModelListConverterTest extends ConversionTestObjects {

    @Mock
    private TagVOToTagDataModelEntityConverter entityConverter;

    @InjectMocks
    private TagVOToTagDataModelListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(TAG_VO)).willReturn(TAG_DATA_MODEL);

        // when
        TagListDataModel result = converter.convert(Collections.singletonList(TAG_VO));

        // then
        assertThat(result, equalTo(TAG_LIST_DATA_MODEL));
    }
}