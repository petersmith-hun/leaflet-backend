package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TagAssignmentRequestConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TagAssignmentRequestConverterTest extends ConversionTestObjects {

    @InjectMocks
    private TagAssignmentRequestConverter converter;

    @Test
    public void shouldConvert() {

        // when
        TagAssignmentVO result = converter.convert(TAG_ASSIGNMENT_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(TAG_ASSIGNMENT_VO));
    }
}