package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.service.vo.EntryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link EntryToEntryVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryToEntryVOConverterTest extends ConversionTestObjects {

    @Mock
    private UserToUserVOConverter userToUserVOConverter;

    @Mock
    private CategoryToCategoryVOConverter categoryToCategoryVOConverter;

    @Mock
    private UploadedFileToUploadedFileVOConverter uploadedFileToUploadedFileVOConverter;

    @Mock
    private TagToTagVOConverter tagToTagVOConverter;

    @InjectMocks
    private EntryToEntryVOConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(categoryToCategoryVOConverter.convert(CATEGORY_MINIMUM)).willReturn(CATEGORY_VO);
        given(userToUserVOConverter.convert(USER_MINIMUM)).willReturn(USER_VO);

        // when
        EntryVO result = converter.convert(ENTRY_BASE_DATA);

        // then
        assertThat(result, equalTo(ENTRY_VO));
    }

    @Test
    public void shouldConvertMinimumVO() {

        // when
        EntryVO result = converter.convert(ENTRY_MINIMUM_NO_NPE);

        // then
        assertThat(result, equalTo(ENTRY_VO_FOR_MINIMUM_NO_NPE_MODEL));
    }
}