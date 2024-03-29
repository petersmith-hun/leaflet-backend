package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CategoryVOToCategoryDataModelListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CategoryVOToCategoryDataModelListConverterTest extends ConversionTestObjects {

    @Mock
    private CategoryVOToCategoryDataModelEntityConverter entityConverter;

    @InjectMocks
    private CategoryVOToCategoryDataModelListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(CATEGORY_VO)).willReturn(CATEGORY_DATA_MODEL);

        // when
        CategoryListDataModel result = converter.convert(Collections.singletonList(CATEGORY_VO));

        // then
        assertThat(result, equalTo(CATEGORY_LIST_DATA_MODEL));
    }
}