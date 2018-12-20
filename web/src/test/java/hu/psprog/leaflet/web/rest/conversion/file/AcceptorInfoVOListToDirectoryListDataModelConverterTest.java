package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.DirectoryListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link AcceptorInfoVOListToDirectoryListDataModelConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class AcceptorInfoVOListToDirectoryListDataModelConverterTest extends ConversionTestObjects {

    @Mock
    private AcceptorInfoVOToDirectoryDataModelConverter entityConverter;

    @InjectMocks
    private AcceptorInfoVOListToDirectoryListDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(ACCEPTOR_INFO_VO)).willReturn(DIRECTORY_DATA_MODEL);

        // then
        DirectoryListDataModel result = converter.convert(Collections.singletonList(ACCEPTOR_INFO_VO));

        // then
        assertThat(result, equalTo(DIRECTORY_LIST_DATA_MODEL));
    }
}