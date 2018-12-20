package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CategoryCreateRequestModelToCategoryVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryCreateRequestModelToCategoryVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private CategoryCreateRequestModelToCategoryVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        CategoryVO result = converter.convert(CATEGORY_CREATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(CATEGORY_VO_FOR_CREATE));
    }
}