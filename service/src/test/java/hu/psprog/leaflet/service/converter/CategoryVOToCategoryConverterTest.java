package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CategoryToCategoryVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryVOToCategoryConverterTest extends ConversionTestObjects {

    @InjectMocks
    private CategoryVOToCategoryConverter converter;

    @Test
    public void shouldConvert() {

        // when
        Category result = converter.convert(CATEGORY_VO);

        // then
        assertThat(result, equalTo(CATEGORY));
    }
}