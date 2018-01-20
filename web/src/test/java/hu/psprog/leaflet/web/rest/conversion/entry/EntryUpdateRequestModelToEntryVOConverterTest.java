package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EntryUpdateRequestModelToEntryVOConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryUpdateRequestModelToEntryVOConverterTest extends ConversionTestObjects {

    @InjectMocks
    private EntryUpdateRequestModelToEntryVOConverter converter;

    @Test
    public void shouldConvertCreateRequest() {

        // when
        EntryVO result = converter.convert(ENTRY_CREATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(ENTRY_VO_FOR_CREATE));
    }

    @Test
    public void shouldConvertUpdateRequest() {

        // when
        EntryVO result = converter.convert(ENTRY_UPDATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(ENTRY_VO_FOR_UPDATE));
    }
}