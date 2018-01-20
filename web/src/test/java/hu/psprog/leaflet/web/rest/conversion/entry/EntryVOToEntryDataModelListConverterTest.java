package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link EntryVOToEntryDataModelListConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryVOToEntryDataModelListConverterTest extends ConversionTestObjects {

    @Mock
    private EntryVOToEntryDataModelEntityConverter entityConverter;

    @InjectMocks
    private EntryVOToEntryDataModelListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(ENTRY_VO)).willReturn(ENTRY_DATA_MODEL);

        // when
        EntryListDataModel result = converter.convert(Collections.singletonList(ENTRY_VO));

        // then
        assertThat(result, equalTo(ENTRY_LIST_DATA_MODEL));
    }
}