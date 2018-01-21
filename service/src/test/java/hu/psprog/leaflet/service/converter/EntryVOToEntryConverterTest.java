package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EntryVOToEntryConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryVOToEntryConverterTest extends ConversionTestObjects {

    @InjectMocks
    private EntryVOToEntryConverter converter;

    @Test
    public void shouldConvertFullVO() {

        // when
        Entry result = converter.convert(ENTRY_VO);

        // then
        assertThat(result, equalTo(ENTRY_BASE_DATA));
    }

    @Test
    public void shouldConvertMinimumVO() {

        // when
        Entry result = converter.convert(ENTRY_MINIMUM_VO);

        // then
        assertThat(result, equalTo(ENTRY_MINIMUM));
    }
}