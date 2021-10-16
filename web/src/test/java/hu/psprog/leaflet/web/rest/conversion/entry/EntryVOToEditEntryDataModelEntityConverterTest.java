package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.web.rest.conversion.file.UploadedFileVOToFileDataModelConverter;
import hu.psprog.leaflet.web.rest.conversion.tag.TagVOToTagDataModelEntityConverter;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EntryVOToEditEntryDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntryVOToEditEntryDataModelEntityConverterTest extends ConversionTestObjects {

    @Mock
    private UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter;

    @Mock
    private TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter;

    @InjectMocks
    private EntryVOToEditEntryDataModelEntityConverter converter;

    @Test
    public void shouldConvert() {

        // when
        EditEntryDataModel result = converter.convert(ENTRY_VO);

        // then
        assertThat(result, equalTo(EDIT_ENTRY_DATA_MODEL));
    }
}