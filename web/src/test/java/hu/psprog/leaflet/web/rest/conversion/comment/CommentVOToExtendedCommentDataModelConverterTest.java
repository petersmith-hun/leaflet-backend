package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.web.rest.conversion.entry.EntryVOToEntryDataModelEntityConverter;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CommentVOToExtendedCommentDataModelConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentVOToExtendedCommentDataModelConverterTest extends ConversionTestObjects {

    @Mock
    private EntryVOToEntryDataModelEntityConverter entryConverter;

    @InjectMocks
    private CommentVOToExtendedCommentDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entryConverter.convert(ENTRY_VO)).willReturn(ENTRY_DATA_MODEL);

        // when
        ExtendedCommentDataModel result = converter.convert(COMMENT_VO);

        // then
        assertThat(result, equalTo(EXTENDED_COMMENT_DATA_MODEL));
    }
}