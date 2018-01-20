package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CategoryVOToCategoryDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryVOToCategoryDataModelEntityConverterTest extends ConversionTestObjects {

    @InjectMocks
    private CategoryVOToCategoryDataModelEntityConverter converter;

    @Test
    public void shouldConvert() {

        // when
        CategoryDataModel result = converter.convert(CATEGORY_VO);

        // then
        assertThat(result, equalTo(CATEGORY_DATA_MODEL));
    }
}