package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.service.vo.CategoryVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CategoryToCategoryVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CategoryToCategoryVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private CategoryToCategoryVOConverter converter;

    @Test
    public void shouldConvert() {

        // when
        CategoryVO result = converter.convert(CATEGORY);

        // then
        assertThat(result, equalTo(CATEGORY_VO));
    }
}