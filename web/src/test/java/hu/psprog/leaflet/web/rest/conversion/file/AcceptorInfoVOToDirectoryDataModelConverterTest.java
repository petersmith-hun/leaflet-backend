package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.DirectoryDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AcceptorInfoVOToDirectoryDataModelConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class AcceptorInfoVOToDirectoryDataModelConverterTest extends ConversionTestObjects {

    @InjectMocks
    private AcceptorInfoVOToDirectoryDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // when
        DirectoryDataModel result = converter.convert(ACCEPTOR_INFO_VO);

        // then
        assertThat(result, equalTo(DIRECTORY_DATA_MODEL));
    }
}