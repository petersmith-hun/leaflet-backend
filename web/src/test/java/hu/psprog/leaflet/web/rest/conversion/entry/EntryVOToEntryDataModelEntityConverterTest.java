package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.web.rest.conversion.file.UploadedFileVOToFileDataModelConverter;
import hu.psprog.leaflet.web.rest.conversion.tag.TagVOToTagDataModelEntityConverter;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EntryVOToEntryDataModelEntityConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryVOToEntryDataModelEntityConverterTest extends ConversionTestObjects {

    @Mock
    private UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter;

    @Mock
    private TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter;

    @InjectMocks
    private EntryVOToEntryDataModelEntityConverter converter;

    @Test
    public void shouldConvert() {

        // when
        EntryDataModel result = converter.convert(ENTRY_VO);

        // then
        assertThat(result, equalTo(ENTRY_DATA_MODEL));
    }
}