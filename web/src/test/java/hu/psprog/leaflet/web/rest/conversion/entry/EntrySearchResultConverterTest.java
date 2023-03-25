package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.entry.EntrySearchResultDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link EntrySearchResultConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntrySearchResultConverterTest extends ConversionTestObjects {

    @Mock
    private EntryVOToEditEntryDataModelEntityConverter entryVOToEditEntryDataModelEntityConverter;

    @InjectMocks
    private EntrySearchResultConverter converter;

    @Test
    public void shouldConvertListOfEntries() {

        // given
        List<EntryVO> source = List.of(ENTRY_VO_FOR_UPDATE);
        EntrySearchResultDataModel expectedResult = EntrySearchResultDataModel.getBuilder()
                .withEntries(List.of(EDIT_ENTRY_DATA_MODEL))
                .build();

        given(entryVOToEditEntryDataModelEntityConverter.convert(ENTRY_VO_FOR_UPDATE)).willReturn(EDIT_ENTRY_DATA_MODEL);

        // when
        EntrySearchResultDataModel result = converter.convert(source);

        // then
        assertThat(result, equalTo(expectedResult));
    }
}
